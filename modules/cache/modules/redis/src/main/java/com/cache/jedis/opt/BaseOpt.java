package com.cache.jedis.opt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.List;

public class BaseOpt {

    protected static Logger logger = LoggerFactory.getLogger(BaseOpt.class);

    protected JedisPool jedisPool;

    protected ShardedJedisPool shardedJedisPool;

    protected boolean sharded = false;

    public BaseOpt(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public BaseOpt(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
        this.sharded = true;
    }

    //执行redis操作
    public <T> T execute(String key, RedisInvoker<T> redisInvoker) {
        ShardedJedis sjd = null ;
        Jedis jd = null;
        try {
            if(sharded) {
                sjd = shardedJedisPool.getResource();
                jd = sjd.getShard(key);
            } else {
                jd = jedisPool.getResource();
            }
            T result = redisInvoker.invoke(jd);
            return result;
        } catch (JedisConnectionException jce) {
            logger.error("BaseOpt execute occur JedisConnectionException, key="+key, jce);
            throw jce;
        } catch (JedisDataException jde) {
            logger.error("BaseOpt execute occur JedisDataException, key="+key,jde);
            throw jde;
        } catch (Exception e) {
            logger.error("BaseOpt execute occur Exception, key="+key,e);
            throw new RuntimeException(e);
        } finally {
            this.close(jd,sjd);
        }
    }

    //释放连接
    private void close(Jedis jd ,ShardedJedis sjd) {
        if(sharded) {
            if(sjd!=null) {
                sjd.close();
            }
        } else {
            if(jd!=null) {
                jd.close();
            }
        }
    }

    public Long expire(String key, int seconds) {
        Long result = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.expire(key,seconds);
                return result;
            }
        });
        return result;
    }

    public Long expireAt(String key, long unixTime) {
        Long result = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.expireAt(key,unixTime);
                return result;
            }
        });
        return result;
    }

    public String get(String key) {
        String result = this.execute(key, new RedisInvoker<String>() {
            @Override
            public String invoke(Jedis jedis) {
                String result = jedis.get(key);
                return result;
            }
        });
        return result;
    }

    public List<String> mget(String... keys) {
        String firstKey = keys[0];
        List<String> result = this.execute(firstKey, new RedisInvoker<List<String>>() {
            @Override
            public List<String> invoke(Jedis jedis) {
                List<String> result = jedis.mget(keys);
                return result;
            }
        });
        return result;
    }

    /**
     * 设置key的缓存值
     * @param expireTime 过期时长，单位:秒
     * @return
     */
    public String set(String key, String value, Integer expireTime) {
        String result = this.execute(key, new RedisInvoker<String>() {
            @Override
            public String invoke(Jedis jedis) {
                String result = jedis.set(key,value);
                if(expireTime!=null) {
                    jedis.expire(key, expireTime);
                }
                return result;
            }
        });
        return result;
    }

    public String set(String key, String value, String nxxx, String expx,long time) {
        String result = this.execute(key, new RedisInvoker<String>() {
            @Override
            public String invoke(Jedis jedis) {
                String result = jedis.set(key, value, nxxx, expx, time);
                return result;
            }
        });
        return result;
    }

    public Long del(String key) {
        Long result = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.del(key);
                return result;
            }
        });
        return result;
    }

    public Long incr(String key) {
        Long result = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.incr(key);
                return result;
            }
        });
        return result;
    }
}
