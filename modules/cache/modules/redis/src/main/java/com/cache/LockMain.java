package com.cache;

import com.cache.redis.util.RedisLockUtil;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LockMain {

    private static Logger logger = LoggerFactory.getLogger(LockMain.class);

    public static void main(String args []) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/ApplicationContext-context.xml",
                        "classpath:spring/ApplicationContext-redis.xml"
                });
        applicationContext.start();

        logger.info("LockMain started..............");
        new Thread(new Runnable() {
            public void run() {
                testLock();
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                testLock();
            }
        }).start();
        logger.info("LockMain testLock end..............");
    }

    public static void testLock() {
        String key = "concurrent_lock_001";
         logger.info("RedisLockUtil testLock start ,thread={}",Thread.currentThread().getName());
        boolean lock = RedisLockUtil.lock(key);
        if(lock) {
            try {
                logger.info("RedisLockUtil lock gain success,thread={}",Thread.currentThread().getName());
                ThreadUtil.sleep(10000L);
            } finally {
                //释放锁
                RedisLockUtil.unLock(key);
                logger.info("RedisLockUtil unLock success...............");
            }
        } else {
            logger.info("RedisLockUtil lock gain fail,thread={}",Thread.currentThread().getName());
        }
    }
}
