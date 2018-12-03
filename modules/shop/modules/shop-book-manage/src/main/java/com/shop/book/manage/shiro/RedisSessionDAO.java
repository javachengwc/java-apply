package com.shop.book.manage.shiro;

import com.util.SerializeUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisSessionDAO extends AbstractSessionDAO{

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, byte[]> redisTemplate;

    private String keyPrefix = "shiro_redis_session:";

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        if(session != null){
            logger.info("RedisSessionDAO doCreate,sessionId={}",sessionId);
        }
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            logger.error("RedisSessionDAO doReadSession sessionId is null");
            return null;
        }
        byte[] sessionBytes= redisTemplate.opsForValue().get(this.keyPrefix+sessionId);
        Session session = (Session) SerializeUtil.deserialize(sessionBytes);
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        Serializable sessionId= session==null?null:session.getId();
        if (sessionId== null) {
            logger.error("RedisSessionDAO update session or sessionId is null");
            return;
        }
        logger.info("RedisSessionDAO update,sessionId={}", sessionId);
        this.saveSession(session);
    }

    @Override
    public void delete(Session session) {
        Serializable sessionId= session==null?null:session.getId();
        if (sessionId== null) {
            logger.error("RedisSessionDAO delete session or sessionId is null");
            return;
        }
        logger.debug("RedisSessionDAO delete, sessionId={}", sessionId);
        redisTemplate.delete(this.keyPrefix+session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<>();
        Set<String> keys = redisTemplate.keys(this.keyPrefix + "*");
        if (keys != null && keys.size() > 0) {
            for (String per : keys) {
                byte[] sessionBytes= redisTemplate.opsForValue().get(per);
                Session s = (Session) SerializeUtil.deserialize(sessionBytes);
                sessions.add(s);
            }
        }
        return sessions;
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.error("RedisSessionDAO saveSession session or session id is null");
            return;
        }
        String key = this.keyPrefix+session.getId();
        byte[] value = SerializeUtil.serialize(session);
        session.setTimeout(30*24* 60 * 60 *1000); //30天
        redisTemplate.opsForValue().set(key, value, session.getTimeout(), TimeUnit.MILLISECONDS);
    }
}
