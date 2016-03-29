package com.rule.data.service.core;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.rule.data.handler.Gearman;
import com.rule.data.model.SerDb;
import com.rule.data.model.SerMap;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.model.vo.ReloadInfo;
import com.rule.data.util.ConfigUtil;
import com.rule.data.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.getProperty;

/**
 * 服务提供者
 */
public final class Services {

    static class Config {

        ConcurrentHashMap<String, SerService> serviceConfigStr = new ConcurrentHashMap<String, SerService>();

        ConcurrentHashMap<Integer, String> serviceid2Name = new ConcurrentHashMap<Integer, String>();

        ConcurrentHashMap<String, SerDb> dbConfig = new ConcurrentHashMap<String, SerDb>();

        HashMap<String, String> serviceMapping = new HashMap<String, String>();
    }

    //缓存时间超过此值...
    public static final int RELOAD_CACHETIME_THRESHOLD = 1800; // 缓存预加载的阀值

    //查询时间不超过此值...
    public static final int RELOAD_LATENCY_THRESHOLD = 2000; // 缓存预加载的时延要求

    private static final int PRE_RELOAD_TIME_THRESHOLD = 5 * 60; // 缓存提前预加载的阀值

    private static final int RELOAD_THREAD_COUNT = 2;

    private static final int FIND_CHANGE_PERIOD_SECOND = 60;

    public static final String SERVICE_USER_INFO = "根据用户名查询信息";

    public static final String SERVICE_USER_LIST = "用户列表查询";

    private static Config config = new Config();

    private static volatile int config_cache_second = ConfigUtil.getConfigCacheSecond();

    public static SqlMapClient client;

    private static final Thread loadConfigThread;

    private static final ExecutorService dealChangePool = new ThreadPoolExecutor(RELOAD_THREAD_COUNT  , RELOAD_THREAD_COUNT, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>()
            , new ThreadFactory() {

                    final AtomicInteger poolNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {

                        return new Thread(r, "reload_cache-" + poolNumber.getAndIncrement());
                    }
            });

    private static final Thread findChangeThread;

    private static final ConcurrentLinkedQueue<String> configChanged = new ConcurrentLinkedQueue<String>();

    private static volatile boolean needReload = true;

    static {

        try {
            InputStream is = new FileInputStream(new File(getProperty("user.dir") + File.separator + "mybatis" + File.separator + "dbSource.xml"));

            client = SqlMapClientBuilder.buildSqlMapClient(is);

        } catch (Throwable e) {

            LogUtil.fatal("load dbSource.xml error", e);

            e.printStackTrace(System.out);

            System.exit(-1);
        }
    }

    private static void submitDealChange(Runnable runnable) {
        try {
            dealChangePool.submit(runnable);
        } catch (Exception e) {
            LogUtil.error("error submit deal change, " + String.valueOf(e));
        }
    }

    static {

        loadConfigThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("reload config");

                long time = 0;
                while (true) {
                    try {
                        if (time >= config_cache_second || needReload) {
                            needReload = false;
                            time = 0;
                            configChanged.clear();

                            if (client == null) {
                                throw new NullPointerException("SqlMapClient");
                            }

                            final List<SerService> pos = client.queryForList("ServiceGroupInfoList");
                            final List<SerDb> dbpos = client.queryForList("TServiceDBByList");

                            final ConcurrentHashMap<String, SerService> serviceConfigStrNew = new ConcurrentHashMap<String, SerService>();
                            final ConcurrentHashMap<Integer, String> serviceid2NameNew = new ConcurrentHashMap<Integer, String>();
                            final ConcurrentHashMap<String, SerDb> dbConfigNew = new ConcurrentHashMap<String, SerDb>();
                            final HashMap<String, String> serviceMappingNew = getServiceMapping();

                            for (SerService servicePo : pos) {
                                final SerService oldPo = getService(servicePo.getName());

                                if (oldPo != null && oldPo.equals(servicePo)) {
                                    serviceConfigStrNew.put(servicePo.getName(), oldPo);
                                    serviceid2NameNew.put(servicePo.getServiceID(), oldPo.getName());
                                } else {
                                    serviceConfigStrNew.put(servicePo.getName(), servicePo);
                                    serviceid2NameNew.put(servicePo.getServiceID(), servicePo.getName());
                                }
                            }

                            for (SerDb dbPo : dbpos) {
                                final SerDb oldPo = getDB(dbPo.getDbID());

                                if (oldPo != null && oldPo.equals(dbPo)) {
                                    dbConfigNew.put(dbPo.getDbID(), oldPo);
                                } else {
                                    dbConfigNew.put(dbPo.getDbID(), dbPo);
                                }
                            }

                            Config newC = new Config();
                            newC.dbConfig = dbConfigNew;
                            newC.serviceConfigStr = serviceConfigStrNew;
                            newC.serviceid2Name = serviceid2NameNew;
                            newC.serviceMapping = serviceMappingNew;
                            config = newC;
                        } else {
                            String serviceName;
                            while ((serviceName = configChanged.poll()) != null) {
                                SerService po = (SerService) client.queryForObject("ServiceGroupInfo", serviceName);

                                if (po == null) { // 删除
                                    SerService oldPo = config.serviceConfigStr.remove(serviceName);
                                    if (oldPo != null) {
                                        config.serviceid2Name.remove(oldPo.getServiceID());
                                    }
                                } else {
                                    SerService oldPo = getService(po.getName());
                                    if (oldPo == null) { // 新增
                                        config.serviceConfigStr.put(po.getName(), po);
                                        config.serviceid2Name.put(po.getServiceID(), po.getName());
                                    } else { // 修改
                                        config.serviceConfigStr.put(po.getName(), po);
                                        config.serviceid2Name.put(po.getServiceID(), po.getName());
                                    }
                                }
                            }
                        }
                    } catch (Throwable t) {
                        LogUtil.error("load config error, " + Gearman.simpleString4E(t));
                    } finally {
                        time++;
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            LogUtil.error(String.valueOf(e));
                        }
                    }
                }
            }
        });

        loadConfigThread.start();

        findChangeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("find change");
                final PriorityQueue<ReloadInfo> needReloadQ = new PriorityQueue<ReloadInfo>();

                while (true) {
                    try {
                        Iterator<Map.Entry<String, ConcurrentHashMap<String, D2Data>>> cacheLay1
                                = Cache4BaseService.DATA_CACHE.entrySet().iterator();

                        for (; cacheLay1.hasNext(); ) {
                            Map.Entry<String, ConcurrentHashMap<String, D2Data>> entryLay1 = cacheLay1.next();
                            final SerService po = getService(entryLay1.getKey());

                            if (po != null) {
                                final int cacheTime = po.getCacheTime() == null ? 0 : po.getCacheTime();
                                final long currentTime = System.currentTimeMillis() / 1000;
                                final long timestamp = po.getUpdateTime() == null ? 0L : po.getUpdateTime().getTime();

                                Iterator<Map.Entry<String, D2Data>> cacheLay2
                                        = entryLay1.getValue().entrySet().iterator();

                                for (; cacheLay2.hasNext(); ) {
                                    Map.Entry<String, D2Data> entryLay2 = cacheLay2.next();
                                    final D2Data curD2Data = entryLay2.getValue();
                                    final long timeElapsed = currentTime - curD2Data.createtime; // 已流失的时间
                                    final long timeSurvied = cacheTime - timeElapsed; // 存活时间

                                    if (curD2Data.timestamp != timestamp
                                            || timeElapsed > cacheTime) {
                                        // 已不是最新版本，已过期
                                        cacheLay2.remove(); // 立即移除
                                    } else if (timeSurvied <= PRE_RELOAD_TIME_THRESHOLD
                                            && cacheTime >= RELOAD_CACHETIME_THRESHOLD
                                            && curD2Data.latency <= RELOAD_LATENCY_THRESHOLD
                                            && !curD2Data.isInReload) {
                                        // 提前五分钟开始Reload,
                                        // 如果是缓存时间超过半小时，且reload时延不超过2s
                                        // 如果在Reload中，则不重新Reload
                                        curD2Data.isInReload = true;
                                        needReloadQ.offer(new ReloadInfo(
                                                curD2Data.lastAcTime, po
                                                , curD2Data.parameter
                                                , curD2Data.latency
                                                , curD2Data.createtime));
                                    }
                                }
                            } else { // 已删除，需要移除整个Lay1层的缓存
                                cacheLay1.remove();
                            }
                        }

                        long sum = RELOAD_THREAD_COUNT * FIND_CHANGE_PERIOD_SECOND * 1000;
                        ReloadInfo needReload;

                        while ((needReload = needReloadQ.poll()) != null && sum > 0) {
                            final ReloadInfo willReload = needReload;
                            final SerService po = willReload.po;

                            submitDealChange(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Cache4BaseService.getD2Data(po, willReload.param, willReload.createtime);
                                    } catch (Exception e) {
                                        LogUtil.error(Gearman.simpleString4E(e));
                                    }
                                }
                            });

                            sum -= willReload.latency;
                        }

                        needReloadQ.clear();
                    } catch (Exception e) {
                        LogUtil.error(Gearman.simpleString4E(e));
                    } finally {
                        try {
                            TimeUnit.SECONDS.sleep(FIND_CHANGE_PERIOD_SECOND);
                        } catch (InterruptedException e) {
                            LogUtil.error(String.valueOf(e));
                        }
                    }
                }
            }
        });

        findChangeThread.start();
    }

    private static HashMap<String, String> getServiceMapping() {
        HashMap<String, String> ret = new HashMap<String, String>();
        try {
            final List<SerMap> pos = client.queryForList("ServiceMappingList");

            for (SerMap po : pos) {
                ret.put(po.getServiceInterface(), po.getServiceImplement());
            }
        } catch (SQLException e) {
            LogUtil.error("fail get service mapping, " + String.valueOf(e));
        }

        return ret;
    }

    public static String id2Name(Integer serviceID) {
        if (serviceID == null) {
            return null;
        }

        return config.serviceid2Name.get(serviceID);
    }

    public static SerService getService(String serviceName) {
        if (serviceName == null) {
            return null;
        }

        SerService po = config.serviceConfigStr.get(serviceName);
        return po;
    }

    public static SerDb getDB(String dbID) {
        if (dbID == null) {
            return null;
        }

        return config.dbConfig.get(dbID);
    }


    public static String getServiceMapping(String name) {
        if (name == null) {
            return null;
        }

        return config.serviceMapping.get(name);
    }

}
