package com.cache.spymemcached;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpyMemcachedUtil {

    public static Logger logger = LoggerFactory.getLogger(SpyMemcachedUtil.class);

    public static final int DEFAULT_EXPIRE = 0;

    private static MemcachedClient client;

    static {
        try {
            client = SpyMemcachedUtil.createClient("127.0.0.1:11211,127.0.0.1:11212");
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil static init error,",e);
        }
    }

    public static MemcachedClient createClient(String serverPorts) throws IOException {
        String[] servers = serverPorts.split(",");
        List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>(servers.length);
        for(String server:servers){
            String[] serverPort = server.split(":");
            if(serverPort.length != 2){
                throw new IllegalArgumentException("invalid mc address: "+server);
            }
            int port = Integer.parseInt(serverPort[1]);
            addresses.add(new InetSocketAddress(serverPort[0],port));
        }
        MemcachedClient client = new MemcachedClient(addresses);
        return client;
    }

    public static MemcachedClient createClient(InetSocketAddress... ia) throws IOException {
        MemcachedClient client = new MemcachedClient(ia);
        return client;
    }

    public static MemcachedClient createClient(List<InetSocketAddress> addrs) throws IOException {
        MemcachedClient client = new MemcachedClient(addrs);
        return client;
    }

    private static <T> T callFuture(Future<T> t, T defaultValue) {
        try {
            return t.get();
        } catch (InterruptedException e) {
            return defaultValue;
        } catch (ExecutionException e) {
            return defaultValue;
        }
    }

    public static boolean set(String key, Object value) {
        boolean result = false;
        try {
            result = callFuture(client.set(key, DEFAULT_EXPIRE, value), false);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil set key=" + key + " error : ", e);
        }
        return result;
    }

    public static boolean cas(String key, long casId, Object value){
        try {
            CASResponse response = client.cas(key, casId, value);
            return response == CASResponse.OK || response == CASResponse.NOT_FOUND;
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil cas key=" + key + " error : ", e);
        }
        return false;
    }

    private static int dateToExpire(Date date) {
        return (int) (date.getTime() / 1000);
    }

    public static boolean set(String key, Object value, Date date) {
        boolean result = false;
        try {
            result =  callFuture(client.set(key, dateToExpire(date), value), false);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil set key=" + key + " error : ", e);
        }
        return result;
    }

    public static Object get(String key) {
        Object result = null;
        try {
            result = client.get(key);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil get key=" + key + " error : ", e);
        }
        return result;
    }

    public static CASValue<Object> gets(String key){
        CASValue<Object> result = null;
        try {
            result =  client.gets(key);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil gets key=" + key + " error : ", e);
        }
        return result;
    }

    public static boolean delete(String key) {
        boolean result = false;
        try {
            result =  callFuture(client.delete(key), false);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil delete key=" + key + " error : ", e);
        }
        return result;
    }

    public static Map<String, Object> getMulti(String[] keys) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = client.getBulk(keys);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil add getMulti=" + result + " error : ", e);
        }
        return result;
    }

    public static boolean add(String key, Object value) {
        boolean result = false;
        try {
            result =  callFuture(client.add(key, DEFAULT_EXPIRE, value), false);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil add key=" + key + " error : ", e);
        }
        return result;
    }

    public static boolean add(String key, Object value, Date expdate) {
        boolean result = false;
        try {
            result =  callFuture(client.add(key, dateToExpire(expdate), value), false);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil add key=" + key + " error : ", e);
        }
        return result;
    }

    public static long incr(String key) {
        return incr(key,1);
    }

    public static long incr(String key, long inc) {
        long result = -1L;
        try {
            result =  client.incr(key, inc);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil incr key=" + key + " error : ", e);
        }
        return result;
    }

    public static long decr(String key) {
        return decr(key,1);
    }

    public static long decr(String key, int inc) {
        long result = -1L;
        try {
            result = client.decr(key, inc);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil decr key=" + key + " error : ", e);
        }
        return result;
    }

    public static Future<Boolean> setAsync(String key, Object value) {
        Future<Boolean> result = null;
        try {
            result = client.set(key, DEFAULT_EXPIRE, value);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil setAsync key=" + key + " error : ", e);
        }
        return result;
    }

    public static Future<Boolean> setAsync(String key, Object value, Date expdate) {
        Future<Boolean> result = null;
        try {
            result = client.set(key, dateToExpire(expdate), value);
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil setAsync key=" + key + " error : ", e);
        }
        return result;
    }

    public static boolean flush() {
        try {
            OperationFuture<Boolean> result =  client.flush();
            return result.get();
        } catch (Exception e) {
            logger.error("SpyMemcachedUtil flush " + " error : ", e);
        }
        return false;
    }
}
