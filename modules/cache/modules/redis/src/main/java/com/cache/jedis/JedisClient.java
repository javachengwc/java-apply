package com.cache.jedis;

import com.util.base.ObjectUtil;
import com.util.lang.RunTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;

public class JedisClient {

    private static final Logger logger = LoggerFactory.getLogger(JedisClient.class);

    private JedisPool jedisPools = null;

    private static final JedisClient inst = new JedisClient();

    public static JedisClient getInstance() {
        return inst;
    }

    private JedisClient() {
        initPool();
        RunTimeUtil.addShutdownHook(new Runnable() {
            public void run() {
                if(jedisPools!=null)
                {
                    jedisPools.close();
                }
            }
        });
    }

    private void initPool() {

        // 操作超时时间
        String timeoutStr =JedisConf.getValue("redis.timeout");
        int timeout = ObjectUtil.obj2Integer(timeoutStr,2000);

        // jedis池最大连接数总数
        String maxTotalStr =JedisConf.getValue("redis.jedisPoolConfig.maxTotal");
        int maxTotal = ObjectUtil.obj2Integer(maxTotalStr,8);

        // jedis池最大空闲连接数
        String maxIdleStr =JedisConf.getValue("redis.jedisPoolConfig.maxIdle");
        int maxIdle = ObjectUtil.obj2Integer(maxIdleStr,8);

        // jedis池最少空闲连接数
        String minIdleStr =JedisConf.getValue("redis.jedisPoolConfig.minIdle");
        int minIdle = ObjectUtil.obj2Integer(minIdleStr,0);

        // jedis池没有对象返回时，最大等待时间单位为毫秒
        String maxWaitMillisStr =JedisConf.getValue("redis.jedisPoolConfig.maxWaitTime");
        long maxWaitMillis = ObjectUtil.obj2Long(maxWaitMillisStr,-1l);

        // 在borrow一个jedis时，是否提前进行validate
        boolean testOnBorrow =ObjectUtil.obj2Boolean(JedisConf.getValue("redis.jedisPoolConfig.testOnBorrow"));

        try {
            // jedis连接池
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(maxTotal);
            poolConfig.setMaxIdle(maxIdle);
            poolConfig.setMinIdle(minIdle);
            poolConfig.setMaxWaitMillis(maxWaitMillis);
            poolConfig.setTestOnBorrow(testOnBorrow);

            jedisPools = new JedisPool(poolConfig, JedisConf.getHost(), JedisConf.getPort(), timeout);
            logger.info("JedisClient initPool end................");
        }catch(Exception e)
        {
            logger.error("JedisClient initPool error,",e);
        }
    }

    public interface JedisExecutor<T>
    {
        public T execute(Jedis jedis );
    }

    public <T> T execute(JedisExecutor<T> executor) {
        Jedis jedis = null;
        T result = null;
        try {
            jedis = jedisPools.getResource();
            result = executor.execute(jedis);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    //是否存在key
    public Boolean exists(final String key) {
        return execute( new JedisExecutor<Boolean>() {
            public Boolean execute(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    //设置过期时间
    public Long expire(final String key, final int seconds) {
        return execute(new JedisExecutor<Long>() {
            public Long execute(Jedis jedis) {
                return jedis.expire(key, seconds);
            }
        });
    }

    //删除key
    public Long del(final String key) {
        return execute(new JedisExecutor<Long>() {
            public Long execute(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }

    //获取值
    public String get(final String key) {
        return execute( new JedisExecutor<String>() {
            public String execute(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    //设置值
    public String set(final String key, final String value) {
        return execute(new JedisExecutor<String>() {
            public String execute(Jedis jedis) {
                return jedis.set(key, value);
            }
        });
    }

    public String set(final String key, final String value, final String n, final String exp, final long time) {
        return execute( new JedisExecutor<String>() {
            public String execute(Jedis jedis) {
                return jedis.set(key, value, n, exp, time);
            }
        });
    }

    public Long setnx(final String key, final String value) {
        return execute(new JedisExecutor<Long>() {
            public Long execute(Jedis jedis) {
                return jedis.setnx(key, value);
            }
        });
    }

    public String setex(final String key, final int seconds, final String value) {
        return execute(new JedisExecutor<String>() {
            public String execute(Jedis jedis) {
                return jedis.setex(key, seconds, value);
            }
        });
    }

    //原子加
    public Long incr(final String key) {
        return execute(new JedisExecutor<Long>() {
            public Long execute(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    //原子减
    public Long decr(final String key) {
        return execute(new JedisExecutor<Long>() {
            public Long execute(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    //设置map
    public Long hset(final String key, final String field, final String value) {
        return execute(new JedisExecutor<Long>() {
            public Long execute(Jedis jedis) {
                return jedis.hset(key, field, value);
            }
        });
    }

    //获取map中某key的value
    public String hget(final String key, final String field) {
        return execute(new JedisExecutor<String>() {
            public String execute(Jedis jedis) {
                return jedis.hget(key, field);
            }
        });
    }

    public String hmset(final String key, final Map<String, String> hash) {
        return execute(new JedisExecutor<String>() {
            public String execute(Jedis jedis) {
                return jedis.hmset(key, hash);
            }
        });
    }

    public List<String> hmget(final String key, final String... fields) {
        return execute(new JedisExecutor<List<String>>() {
            public List<String> execute(Jedis jedis) {
                return jedis.hmget(key, fields);
            }
        });
    }

    //获取map
    public Map<String, String> hgetAll(final String key) {
        return execute(new JedisExecutor<Map<String, String>>() {
            public Map<String, String> execute(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public static void main(String args [])
    {
        JedisClient jedisClient = JedisClient.getInstance();
        System.out.println(jedisClient.set("aa","0"));
        System.out.println(jedisClient.incr("aa"));
        System.out.println(jedisClient.incr("aa"));
        System.out.println(jedisClient.decr("aa"));
        System.out.println(jedisClient.decr("aa"));
        System.out.println(jedisClient.decr("aa"));
    }
}
