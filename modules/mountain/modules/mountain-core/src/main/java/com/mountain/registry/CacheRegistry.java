package com.mountain.registry;

import com.mountain.constant.Constant;
import com.mountain.model.SpecUrl;
import com.mountain.util.NamedThreadFactory;
import com.mountain.util.ServiceUrlUtil;
import com.util.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 本地缓存注册
 */
public abstract class CacheRegistry implements  Registry{

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    //url地址分隔符
    private static final char URL_SEPARATOR = ' ';

    //url地址分隔正则表达式匹配任意空白字符
    private static final String URL_SPLIT = "\\s+";

    protected SpecUrl registryUrl;

    private File file;

    protected Properties properties = new Properties();

    //文件缓存定时写入
    private  ExecutorService registryCacheExecutor = Executors.newFixedThreadPool(1, new NamedThreadFactory("CacheRegistry", true));

    //同步或异步保存
    private boolean syncSaveFile ;

    private AtomicLong lastCacheCnt = new AtomicLong();

    //相当于ConcurrentHashSet<SpecUrl>();
    private Set<SpecUrl> registered =Collections.newSetFromMap(new ConcurrentHashMap<SpecUrl,Boolean>());

    private ConcurrentMap<SpecUrl, Set<NotifyListener>> subscribed = new ConcurrentHashMap<SpecUrl, Set<NotifyListener>>();

    private ConcurrentMap<SpecUrl, Map<String, List<SpecUrl>>> notified = new ConcurrentHashMap<SpecUrl, Map<String, List<SpecUrl>>>();

    public CacheRegistry(SpecUrl url) {
        setUrl(url);
        syncSaveFile =url.getParameter("cache_mode", false);
        String fileName = System.getProperty("user.home") + "/.mountain/registry-" + url.getParameter("application","application") + url.getHost() + ".cache";
        logger.info("CacheRegistry fileName="+fileName);
        File file = null;
        if (!StringUtils.isBlank(fileName)) {
            file = new File(fileName);
            if(! file.exists() && file.getParentFile() != null && !file.getParentFile().exists()){
                if(!file.getParentFile().mkdirs()){
                    throw new IllegalArgumentException("CacheRegistry create file parent dir  fail, file=" + file);
                }
            }
        }
        this.file = file;
        PropertiesLoader.loadFile(file,properties);
        notify(url.getBackupUrls());
    }

    protected void setUrl(SpecUrl url) {
        if (url == null) {
            throw new IllegalArgumentException("CacheRegistry setUrl url is null");
        }
        this.registryUrl = url;
    }

    public SpecUrl getUrl() {
        return registryUrl;
    }

    public Set<SpecUrl> getRegistered() {
        return registered;
    }

    public Map<SpecUrl, Set<NotifyListener>> getSubscribed() {
        return subscribed;
    }

    public Map<SpecUrl, Map<String, List<SpecUrl>>> getNotified() {
        return notified;
    }

    public File getCacheFile() {
        return file;
    }

    public Properties getCacheProperties() {
        return properties;
    }

    public AtomicLong getlastCacheCnt(){
        return lastCacheCnt;
    }

    private class CacheTask implements Runnable{
        private long version;
        private CacheTask(long version){
            this.version = version;
        }
        public void run() {
            cache(version);
        }
    }

    public void cache(long version) {
        if(file == null || version < lastCacheCnt.get()){
            return;
        }
        //保存之前先读取一遍，防止多个注册中心之间冲突
        Properties newProperties = PropertiesLoader.loadFile(file);
        try {
            newProperties.putAll(properties);
            File lockfile = new File(file.getAbsolutePath() + ".lock");
            if (!lockfile.exists()) {
                lockfile.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(lockfile, "rw");
            try {
                FileChannel channel = raf.getChannel();
                try {
                    FileLock lock = channel.tryLock();
                    if (lock == null) {
                        throw new IOException("CacheRegistry cache can not lock registry cache file " + file.getAbsolutePath() + ", ignore and retry later, maybe multi java process use this file");
                    }
                    //保存
                    try {
                        if (! file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream outputFile = new FileOutputStream(file);
                        try {
                            newProperties.store(outputFile, "CacheRegistry registry cache file");
                        } finally {
                            outputFile.close();
                        }
                    } finally {
                        lock.release();
                    }
                } finally {
                    channel.close();
                }
            } finally {
                raf.close();
            }
        } catch (Exception e) {
            logger.error("CacheRegistry cache  registry store file fail", e);
            if (version < lastCacheCnt.get()) {
                return;
            } else {
                registryCacheExecutor.execute(new CacheTask(lastCacheCnt.incrementAndGet()));
            }
        }
    }

    public List<SpecUrl> getCacheUrls(SpecUrl url) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key != null && key.length() > 0 && key.equals(url.getInterfaceService())
                    && (Character.isLetter(key.charAt(0)) || key.charAt(0) == '_')
                    && value != null && value.length() > 0) {
                String[] arr = value.trim().split(URL_SPLIT);
                List<SpecUrl> urls = new ArrayList<SpecUrl>();
                for (String u : arr) {
                    urls.add(SpecUrl.valueOf(u));
                }
                return urls;
            }
        }
        return null;
    }

    public List<SpecUrl> query(SpecUrl url) {
        List<SpecUrl> result = new ArrayList<SpecUrl>();
        Map<String, List<SpecUrl>> notifiedUrls = getNotified().get(url);
        if (notifiedUrls != null && notifiedUrls.size() > 0) {
            for (List<SpecUrl> urls : notifiedUrls.values()) {
                for (SpecUrl u : urls) {
                    if (! Constant.EMPTY_PROTOCOL.equals(u.getProtocol())) {
                        result.add(u);
                    }
                }
            }
        } else {
            final AtomicReference<List<SpecUrl>> reference = new AtomicReference<List<SpecUrl>>();
            NotifyListener listener = new NotifyListener() {
                public void notify(List<SpecUrl> urls) {
                    reference.set(urls);
                }
            };
            subscribe(url, listener); // 订阅逻辑保证第一次notify后再返回
            List<SpecUrl> urls = reference.get();
            if (urls != null && urls.size() > 0) {
                for (SpecUrl u : urls) {
                    if (! Constant.EMPTY_PROTOCOL.equals(u.getProtocol())) {
                        result.add(u);
                    }
                }
            }
        }
        return result;
    }

    public void register(SpecUrl url) {
        if (url == null) {
            throw new IllegalArgumentException("CacheRegistry register url == null");
        }
        logger.info("CacheRegistry register url:" + url.toUrlStr());
        registered.add(url);
    }

    public void unregister(SpecUrl url) {
        if (url == null) {
            throw new IllegalArgumentException("CacheRegistry unregister url == null");
        }
        logger.info("CacheRegistry unregister url:" + url);
        registered.remove(url);
    }

    public void subscribe(SpecUrl url, NotifyListener listener) {
        if (url == null) {
            throw new IllegalArgumentException("CacheRegistry subscribe url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("CacheRegistry subscribe listener == null");
        }
        logger.info("CacheRegistry subscribe url:" + url);
        Set<NotifyListener> listeners = subscribed.get(url);
        if (listeners == null) {
            subscribed.putIfAbsent(url, Collections.newSetFromMap(new ConcurrentHashMap<NotifyListener,Boolean>()));
            listeners = subscribed.get(url);
        }
        listeners.add(listener);
    }

    public void unsubscribe(SpecUrl url, NotifyListener listener) {
        if (url == null) {
            throw new IllegalArgumentException("CacheRegistry unsubscribe url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("CacheRegistry unsubscribe listener == null");
        }
        logger.info("CacheRegistry unsubscribe url:" + url);
        Set<NotifyListener> listeners = subscribed.get(url);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    protected void recover() throws Exception {
        // register
        Set<SpecUrl> recoverRegistered = new HashSet<SpecUrl>(getRegistered());
        if (recoverRegistered!=null && recoverRegistered.size()>0) {
            logger.info("CacheRegistry recover register url " + recoverRegistered);
            for (SpecUrl url : recoverRegistered) {
                register(url);
            }
        }
        // subscribe
        Map<SpecUrl, Set<NotifyListener>> recoverSubscribed = new HashMap<SpecUrl, Set<NotifyListener>>(getSubscribed());
        if ( recoverSubscribed!=null && recoverSubscribed.size()>0) {
            logger.info("CacheRegistry recover subscribe url " + recoverSubscribed.keySet());
            for (Map.Entry<SpecUrl, Set<NotifyListener>> entry : recoverSubscribed.entrySet()) {
                SpecUrl url = entry.getKey();
                for (NotifyListener listener : entry.getValue()) {
                    subscribe(url, listener);
                }
            }
        }
    }

    protected static List<SpecUrl> filterEmpty(SpecUrl url, List<SpecUrl> urls) {
        if (urls == null || urls.size() == 0) {
            List<SpecUrl> result = new ArrayList<SpecUrl>(1);
            result.add(url.genUrlWithProtocol(Constant.EMPTY_PROTOCOL));
            return result;
        }
        return urls;
    }

    protected void notify(List<SpecUrl> urls) {
        if(urls == null || urls.isEmpty()) return;

        for (Map.Entry<SpecUrl, Set<NotifyListener>> entry : getSubscribed().entrySet()) {
            SpecUrl url = entry.getKey();

            if(! ServiceUrlUtil.isMatch(url, urls.get(0))) {
                continue;
            }

            Set<NotifyListener> listeners = entry.getValue();
            if (listeners != null) {
                for (NotifyListener listener : listeners) {
                    try {
                        notify(url, listener, filterEmpty(url, urls));
                    } catch (Exception e) {
                        logger.error("CacheRegistry notify error,, urls: " +  urls, e);
                    }
                }
            }
        }
    }

    protected void notify(SpecUrl url, NotifyListener listener, List<SpecUrl> urls) {
        if (url == null) {
            throw new IllegalArgumentException("CacheRegistry notify url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("CacheRegistry notify listener == null");
        }
        int urlsLen =urls ==null?0:urls.size();
        if ( urlsLen==0 && ! Constant.ANY_VALUE.equals(url.getInterface())) {
            logger.warn("CacheRegistry notify urls is null ");
            return;
        }
        logger.info("CacheRegistry notify urls for subscribe url: " + url +",urlsLen="+urlsLen);

        Map<String, List<SpecUrl>> result = new HashMap<String, List<SpecUrl>>();
        for (SpecUrl u : urls) {
            if (ServiceUrlUtil.isMatch(url, u)) {
                logger.info("CacheRegistry notify match url has u-->\r\n "+u);
                String category = u.getParameter(Constant.CATEGORY_KEY, Constant.DEFAULT_CATEGORY);
                List<SpecUrl> categoryList = result.get(category);
                if (categoryList == null) {
                    categoryList = new ArrayList<SpecUrl>();
                    result.put(category, categoryList);
                }
                categoryList.add(u);
            }
        }
        if (result.size() <= 0) {
            logger.info("CacheRegistry notify result size <=0");
            return;
        }
        Map<String, List<SpecUrl>> categoryNotified = notified.get(url);
        if (categoryNotified == null) {
            notified.putIfAbsent(url, new ConcurrentHashMap<String, List<SpecUrl>>());
            categoryNotified = notified.get(url);
        }
        for (Map.Entry<String, List<SpecUrl>> entry : result.entrySet()) {
            String category = entry.getKey();
            List<SpecUrl> categoryList = entry.getValue();
            int categoryUrlSize =categoryList==null?0:categoryList.size();
            logger.info("CacheRegistry notify category= "+category+", category url size ="+categoryUrlSize);
            categoryNotified.put(category, categoryList);
            saveProperties(url);
            listener.notify(categoryList);
        }
    }

    private void saveProperties(SpecUrl url) {
        if (file == null) {
            return;
        }
        try {
            StringBuilder buf = new StringBuilder();
            Map<String, List<SpecUrl>> categoryNotified = notified.get(url);
            if (categoryNotified != null) {
                for (List<SpecUrl> list : categoryNotified.values()) {
                    if(list!=null) {
                        for (SpecUrl per : list) {
                            if (buf.length() > 0) {
                                buf.append(URL_SEPARATOR);
                            }
                            buf.append(per.toUrlStr());
                        }
                    }
                }
            }
            String interfaceService=url.getInterfaceService();
            String info = buf.toString();
            logger.info("CacheRegistry saveProperties interfaceService="+interfaceService+",info=\r\n"+info);
            properties.setProperty(interfaceService,info);
            long version = lastCacheCnt.incrementAndGet();
            if (syncSaveFile) {
                cache(version);
            } else {
                registryCacheExecutor.execute(new CacheTask(version));
            }
        } catch (Exception e) {
            logger.error("CacheRegistry saveProperties error,url:"+url, e);
        }
    }

    public void destroy() {
        logger.info("CacheRegistry destroy begin ,register url:"+getUrl());
        Set<SpecUrl> destroyRegistered = new HashSet<SpecUrl>(getRegistered());
        if (! destroyRegistered.isEmpty()) {
            for (SpecUrl url :destroyRegistered) {
                if (Boolean.valueOf(url.getParameter(Constant.DYNAMIC_KEY, "true")))
                {
                    try {
                        unregister(url);
                    } catch (Exception e) {
                        logger.error("CacheRegistry  unregister error on destroy ,url="+url, e);
                    }
                }
            }
        }
        Map<SpecUrl, Set<NotifyListener>> destroySubscribed = new HashMap<SpecUrl, Set<NotifyListener>>(getSubscribed());
        if (! destroySubscribed.isEmpty()) {
            for (Map.Entry<SpecUrl, Set<NotifyListener>> entry : destroySubscribed.entrySet()) {
                SpecUrl url = entry.getKey();
                for (NotifyListener listener : entry.getValue()) {
                    try {
                        unsubscribe(url, listener);
                    } catch (Exception  e) {
                        logger.error("CacheRegistry unsubscribe error on destroy ,url=" +url, e);
                    }
                }
            }
        }
    }

    public String toString() {
        return getUrl().toString();
    }

}
