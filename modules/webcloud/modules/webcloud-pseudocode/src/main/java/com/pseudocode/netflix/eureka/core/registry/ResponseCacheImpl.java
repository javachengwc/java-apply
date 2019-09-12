package com.pseudocode.netflix.eureka.core.registry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPOutputStream;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.netflix.servo.annotations.DataSourceType;
import com.netflix.servo.annotations.Monitor;
import com.netflix.servo.monitor.Monitors;
import com.netflix.servo.monitor.Stopwatch;
import com.netflix.servo.monitor.Timer;
import com.pseudocode.netflix.eureka.client.appinfo.EurekaAccept;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.discovery.shared.Application;
import com.pseudocode.netflix.eureka.client.discovery.shared.Applications;
import com.pseudocode.netflix.eureka.core.EurekaServerConfig;
import com.pseudocode.netflix.eureka.core.Version;
import com.pseudocode.netflix.eureka.core.resources.CurrentRequestVersion;
import com.pseudocode.netflix.eureka.core.resources.ServerCodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseCacheImpl implements ResponseCache {

    private static final Logger logger = LoggerFactory.getLogger(ResponseCacheImpl.class);

    public static final String ALL_APPS = "ALL_APPS";
    public static final String ALL_APPS_DELTA = "ALL_APPS_DELTA";

    private static final AtomicLong versionDeltaLegacy = new AtomicLong(0);
    private static final AtomicLong versionDeltaWithRegionsLegacy = new AtomicLong(0);

    private static final String EMPTY_PAYLOAD = "";
    private final java.util.Timer timer = new java.util.Timer("Eureka-CacheFillTimer", true);
    private final AtomicLong versionDelta = new AtomicLong(0);
    private final AtomicLong versionDeltaWithRegions = new AtomicLong(0);

    private final Timer serializeAllAppsTimer = Monitors.newTimer("serialize-all");
    private final Timer serializeDeltaAppsTimer = Monitors.newTimer("serialize-all-delta");
    private final Timer serializeAllAppsWithRemoteRegionTimer = Monitors.newTimer("serialize-all_remote_region");
    private final Timer serializeDeltaAppsWithRemoteRegionTimer = Monitors.newTimer("serialize-all-delta_remote_region");
    private final Timer serializeOneApptimer = Monitors.newTimer("serialize-one");
    private final Timer serializeViptimer = Monitors.newTimer("serialize-one-vip");
    private final Timer compressPayloadTimer = Monitors.newTimer("compress-payload");

    private final Multimap<Key, Key> regionSpecificKeys =
            Multimaps.newListMultimap(new ConcurrentHashMap<Key, Collection<Key>>(), new Supplier<List<Key>>() {
                @Override
                public List<Key> get() {
                    return new CopyOnWriteArrayList<Key>();
                }
            });

    //只读缓存
    private final ConcurrentMap<Key, Value> readOnlyCacheMap = new ConcurrentHashMap<Key, Value>();

    //读写缓存（固定过期）
    private final LoadingCache<Key, Value> readWriteCacheMap;

    //是否使用只读缓存
    private final boolean shouldUseReadOnlyResponseCache;

    private final AbstractInstanceRegistry registry;
    private final EurekaServerConfig serverConfig;
    private final ServerCodecs serverCodecs;

    ResponseCacheImpl(EurekaServerConfig serverConfig, ServerCodecs serverCodecs, AbstractInstanceRegistry registry) {
        this.serverConfig = serverConfig;
        this.serverCodecs = serverCodecs;
        this.shouldUseReadOnlyResponseCache = serverConfig.shouldUseReadOnlyResponseCache();
        this.registry = registry;

        long responseCacheUpdateIntervalMs = serverConfig.getResponseCacheUpdateIntervalMs();
        this.readWriteCacheMap = CacheBuilder.newBuilder().initialCapacity(1000)
                         //读写缓存过期时间
                        .expireAfterWrite(serverConfig.getResponseCacheAutoExpirationInSeconds(), TimeUnit.SECONDS)
                        .removalListener(new RemovalListener<Key, Value>() {
                            @Override
                            public void onRemoval(RemovalNotification<Key, Value> notification) {
                                Key removedKey = notification.getKey();
                                if (removedKey.hasRegions()) {
                                    Key cloneWithNoRegions = removedKey.cloneWithoutRegions();
                                    regionSpecificKeys.remove(cloneWithNoRegions, removedKey);
                                }
                            }
                        })
                        .build(new CacheLoader<Key, Value>() {
                            @Override
                            public Value load(Key key) throws Exception {
                                if (key.hasRegions()) {
                                    Key cloneWithNoRegions = key.cloneWithoutRegions();
                                    regionSpecificKeys.put(cloneWithNoRegions, key);
                                }
                                //生成注册应用的缓存值
                                Value value = generatePayload(key);
                                return value;
                            }
                        });

        if (shouldUseReadOnlyResponseCache) {
            //定时任务刷新只读缓存
            timer.schedule(getCacheUpdateTask(),
                    new Date(((System.currentTimeMillis() / responseCacheUpdateIntervalMs) * responseCacheUpdateIntervalMs)
                            + responseCacheUpdateIntervalMs),
                    responseCacheUpdateIntervalMs);
        }

        try {
            Monitors.registerObject(this);
        } catch (Throwable e) {
            logger.warn("Cannot register the JMX monitor for the InstanceRegistry", e);
        }
    }

    private TimerTask getCacheUpdateTask() {
        return new TimerTask() {
            @Override
            public void run() {
                logger.debug("Updating the client cache from response cache");
                for (Key key : readOnlyCacheMap.keySet()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Updating the client cache from response cache for key : {} {} {} {}",
                                key.getEntityType(), key.getName(), key.getVersion(), key.getType());
                    }
                    //对比 readWriteCacheMap 和 readOnlyCacheMap 的缓存值，若不一致，以前者为主。
                    //通过这样的方式，实现了readOnlyCacheMap的定时过期
                    try {
                        CurrentRequestVersion.set(key.getVersion());
                        Value cacheValue = readWriteCacheMap.get(key);
                        Value currentCacheValue = readOnlyCacheMap.get(key);
                        if (cacheValue != currentCacheValue) {
                            readOnlyCacheMap.put(key, cacheValue);
                        }
                    } catch (Throwable th) {
                        logger.error("Error while updating the client cache from response cache for key {}", key.toStringCompact(), th);
                    }
                }
            }
        };
    }

    public String get(final Key key) {
        return get(key, shouldUseReadOnlyResponseCache);
    }

    @VisibleForTesting
    String get(final Key key, boolean useReadOnlyCache) {
        Value payload = getValue(key, useReadOnlyCache);
        if (payload == null || payload.getPayload().equals(EMPTY_PAYLOAD)) {
            return null;
        } else {
            return payload.getPayload();
        }
    }

    public byte[] getGZIP(Key key) {
        Value payload = getValue(key, shouldUseReadOnlyResponseCache);
        if (payload == null) {
            return null;
        }
        return payload.getGzipped();
    }

    //应用实例注册、下线、过期时，调用invalidate()方法，主动过期读写缓存readWriteCacheMap
    @Override
    public void invalidate(String appName,String vipAddress,String secureVipAddress) {
        for (Key.KeyType type : Key.KeyType.values()) {
            for (Version v : Version.values()) {
                invalidate(
                        new Key(Key.EntityType.Application, appName, type, v, EurekaAccept.full),
                        new Key(Key.EntityType.Application, appName, type, v, EurekaAccept.compact),
                        new Key(Key.EntityType.Application, ALL_APPS, type, v, EurekaAccept.full),
                        new Key(Key.EntityType.Application, ALL_APPS, type, v, EurekaAccept.compact),
                        new Key(Key.EntityType.Application, ALL_APPS_DELTA, type, v, EurekaAccept.full),
                        new Key(Key.EntityType.Application, ALL_APPS_DELTA, type, v, EurekaAccept.compact)
                );
                if (null != vipAddress) {
                    invalidate(new Key(Key.EntityType.VIP, vipAddress, type, v, EurekaAccept.full));
                }
                if (null != secureVipAddress) {
                    invalidate(new Key(Key.EntityType.SVIP, secureVipAddress, type, v, EurekaAccept.full));
                }
            }
        }
    }

    //过期读写缓存readWriteCacheMap中的keys
    public void invalidate(Key... keys) {
        for (Key key : keys) {
            logger.debug("Invalidating the response cache key : {} {} {} {}, {}",
                    key.getEntityType(), key.getName(), key.getVersion(), key.getType(), key.getEurekaAccept());

            readWriteCacheMap.invalidate(key);
            Collection<Key> keysWithRegions = regionSpecificKeys.get(key);
            if (null != keysWithRegions && !keysWithRegions.isEmpty()) {
                for (Key keysWithRegion : keysWithRegions) {
                    logger.debug("Invalidating the response cache key : {} {} {} {} {}",
                            key.getEntityType(), key.getName(), key.getVersion(), key.getType(), key.getEurekaAccept());
                    readWriteCacheMap.invalidate(keysWithRegion);
                }
            }
        }
    }

    @Override
    public AtomicLong getVersionDelta() {
        return versionDelta;
    }

    @Override
    public AtomicLong getVersionDeltaWithRegions() {
        return versionDeltaWithRegions;
    }

    @Deprecated
    public static AtomicLong getVersionDeltaStatic() {
        return versionDeltaLegacy;
    }

    @Deprecated
    public static AtomicLong getVersionDeltaWithRegionsLegacy() {
        return versionDeltaWithRegionsLegacy;
    }

    @Monitor(name = "responseCacheSize", type = DataSourceType.GAUGE)
    public int getCurrentSize() {
        return readWriteCacheMap.asMap().size();
    }

    @VisibleForTesting
    Value getValue(final Key key, boolean useReadOnlyCache) {
        Value payload = null;
        try {
            if (useReadOnlyCache) {
                //先读取readOnlyCacheMap
                final Value currentPayload = readOnlyCacheMap.get(key);
                if (currentPayload != null) {
                    payload = currentPayload;
                } else {
                    //读取不到才读取readWriteCacheMap，并设置到 readOnlyCacheMap
                    payload = readWriteCacheMap.get(key);
                    readOnlyCacheMap.put(key, payload);
                }
            } else {
                payload = readWriteCacheMap.get(key);
            }
        } catch (Throwable t) {
            logger.error("Cannot get value for key : {}", key, t);
        }
        return payload;
    }

    //将注册的应用集合转换成缓存值
    private String getPayLoad(Key key, Applications apps) {
        //编码器
        EncoderWrapper encoderWrapper = serverCodecs.getEncoder(key.getType(), key.getEurekaAccept());
        String result;
        try {
            //编码
            result = encoderWrapper.encode(apps);
        } catch (Exception e) {
            logger.error("Failed to encode the payload for all apps", e);
            return "";
        }
        if(logger.isDebugEnabled()) {
            logger.debug("New application cache entry {} with apps hashcode {}", key.toStringCompact(), apps.getAppsHashCode());
        }
        return result;
    }

    private String getPayLoad(Key key, Application app) {
        if (app == null) {
            return EMPTY_PAYLOAD;
        }

        EncoderWrapper encoderWrapper = serverCodecs.getEncoder(key.getType(), key.getEurekaAccept());
        try {
            return encoderWrapper.encode(app);
        } catch (Exception e) {
            logger.error("Failed to encode the payload for application {}", app.getName(), e);
            return "";
        }
    }

    //生成注册应用的缓存值
    private Value generatePayload(Key key) {
        Stopwatch tracer = null;
        try {
            String payload;
            switch (key.getEntityType()) {
                case Application:
                    boolean isRemoteRegionRequested = key.hasRegions();

                    if (ALL_APPS.equals(key.getName())) {
                    //全量
                        if (isRemoteRegionRequested) {
                            tracer = serializeAllAppsWithRemoteRegionTimer.start();
                            payload = getPayLoad(key, registry.getApplicationsFromMultipleRegions(key.getRegions()));
                        } else {
                            tracer = serializeAllAppsTimer.start();
                            payload = getPayLoad(key, registry.getApplications());
                        }
                    } else if (ALL_APPS_DELTA.equals(key.getName())) {
                    //增量
                        if (isRemoteRegionRequested) {
                            tracer = serializeDeltaAppsWithRemoteRegionTimer.start();
                            versionDeltaWithRegions.incrementAndGet();
                            versionDeltaWithRegionsLegacy.incrementAndGet();
                            payload = getPayLoad(key, registry.getApplicationDeltasFromMultipleRegions(key.getRegions()));
                        } else {
                            tracer = serializeDeltaAppsTimer.start();
                            versionDelta.incrementAndGet();
                            versionDeltaLegacy.incrementAndGet();
                            payload = getPayLoad(key, registry.getApplicationDeltas());
                        }
                    } else {
                        tracer = serializeOneApptimer.start();
                        payload = getPayLoad(key, registry.getApplication(key.getName()));
                    }
                    break;
                case VIP:
                case SVIP:
                    tracer = serializeViptimer.start();
                    payload = getPayLoad(key, getApplicationsForVip(key, registry));
                    break;
                default:
                    logger.error("Unidentified entity type: {} found in the cache key.", key.getEntityType());
                    payload = "";
                    break;
            }
            return new Value(payload);
        } finally {
            if (tracer != null) {
                tracer.stop();
            }
        }
    }

    private static Applications getApplicationsForVip(Key key, AbstractInstanceRegistry registry) {
        logger.debug("Retrieving applications from registry for key : {} {} {} {}",
                key.getEntityType(), key.getName(), key.getVersion(), key.getType());
        Applications toReturn = new Applications();
        Applications applications = registry.getApplications();
        for (Application application : applications.getRegisteredApplications()) {
            Application appToAdd = null;
            for (InstanceInfo instanceInfo : application.getInstances()) {
                String vipAddress;
                if (Key.EntityType.VIP.equals(key.getEntityType())) {
                    vipAddress = instanceInfo.getVIPAddress();
                } else if (Key.EntityType.SVIP.equals(key.getEntityType())) {
                    vipAddress = instanceInfo.getSecureVipAddress();
                } else {
                    // should not happen, but just in case.
                    continue;
                }

                if (null != vipAddress) {
                    String[] vipAddresses = vipAddress.split(",");
                    Arrays.sort(vipAddresses);
                    if (Arrays.binarySearch(vipAddresses, key.getName()) >= 0) {
                        if (null == appToAdd) {
                            appToAdd = new Application(application.getName());
                            toReturn.addApplication(appToAdd);
                        }
                        appToAdd.addInstance(instanceInfo);
                    }
                }
            }
        }
        toReturn.setAppsHashCode(toReturn.getReconcileHashCode());
        logger.debug(
                "Retrieved applications from registry for key : {} {} {} {}, reconcile hashcode: {}",
                key.getEntityType(), key.getName(), key.getVersion(), key.getType(),
                toReturn.getReconcileHashCode());
        return toReturn;
    }

    public class Value {

        //原始值
        private final String payload;

        //gzip压缩后的值
        private byte[] gzipped;

        public Value(String payload) {
            this.payload = payload;
            if (!EMPTY_PAYLOAD.equals(payload)) {
                Stopwatch tracer = compressPayloadTimer.start();
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    GZIPOutputStream out = new GZIPOutputStream(bos);
                    byte[] rawBytes = payload.getBytes();
                    out.write(rawBytes);
                    // Finish creation of gzip file
                    out.finish();
                    out.close();
                    bos.close();
                    gzipped = bos.toByteArray();
                } catch (IOException e) {
                    gzipped = null;
                } finally {
                    if (tracer != null) {
                        tracer.stop();
                    }
                }
            } else {
                gzipped = null;
            }
        }

        public String getPayload() {
            return payload;
        }

        public byte[] getGzipped() {
            return gzipped;
        }

    }

}

