package com.pseudocode.netflix.eureka.client.discovery.util;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//接口限流--令牌桶算法
//令牌桶算法的原理是系统会以一个恒定的速度往桶里放入令牌，而如果请求需要被处理，则需要先从桶里获取一个令牌，当桶里没有令牌可取时，则拒绝服务
public class RateLimiter {

    //速率单位转换成毫秒
    private final long rateToMsConversion;

    private final AtomicInteger consumedTokens = new AtomicInteger();

    //最后填充令牌的时间
    private final AtomicLong lastRefillTime = new AtomicLong(0);

    @Deprecated
    public RateLimiter() {
        this(TimeUnit.SECONDS);
    }

    public RateLimiter(TimeUnit averageRateUnit) {
        switch (averageRateUnit) {
            case SECONDS:
                rateToMsConversion = 1000;
                break;
            case MINUTES:
                rateToMsConversion = 60 * 1000;
                break;
            default:
                throw new IllegalArgumentException("TimeUnit of " + averageRateUnit + " is not supported");
        }
    }

    //获取令牌，并返回是否获取成功
    public boolean acquire(int burstSize, long averageRate) {
        return acquire(burstSize, averageRate, System.currentTimeMillis());
    }

    //burstSize--请求的令牌桶上限,averageRate--令牌填充平均速率,比如2000，表示averageRateUnit内可填充averageRate个令牌
    public boolean acquire(int burstSize, long averageRate, long currentTimeMillis) {
        if (burstSize <= 0 || averageRate <= 0) { // Instead of throwing exception, we just let all the traffic go
            return true;
        }
        //填充令牌
        refillToken(burstSize, averageRate, currentTimeMillis);

        //消费令牌
        return consumeToken(burstSize);
    }

    //填充令牌
    private void refillToken(int burstSize, long averageRate, long currentTimeMillis) {

        //最后填充令牌的时间
        long refillTime = lastRefillTime.get();

        //过去多少毫秒
        long timeDelta = currentTimeMillis - refillTime;

        //这段时间内可填充令牌数
        long newTokens = timeDelta * averageRate / rateToMsConversion;
        if (newTokens > 0) {
            //新的填充令牌的时间
            long newRefillTime = refillTime == 0
                    ? currentTimeMillis
                    : refillTime + newTokens * rateToMsConversion / averageRate;

            //CAS保证有且只有一个线程进入填充
            if (lastRefillTime.compareAndSet(refillTime, newRefillTime)) {
                while (true) {
                    //净消耗令牌数量
                    int currentLevel = consumedTokens.get();
                    int adjustedLevel = Math.min(currentLevel, burstSize); // In case burstSize decreased
                    int newLevel = (int) Math.max(0, adjustedLevel - newTokens);
                    //避免和正在消费令牌的线程冲突
                    if (consumedTokens.compareAndSet(currentLevel, newLevel)) {
                        return;
                    }
                }
            }
        }
    }

    //消费令牌
    private boolean consumeToken(int burstSize) {
        while (true) {
            int currentLevel = consumedTokens.get();
            // 单位时间内净消耗令牌数量超过burstSize
            if (currentLevel >= burstSize) {
                return false;
            }
            //CAS避免和正在消费令牌或者填充令牌的线程冲突
            if (consumedTokens.compareAndSet(currentLevel, currentLevel + 1)) {
                return true;
            }
        }
    }

    public void reset() {
        consumedTokens.set(0);
        lastRefillTime.set(0);
    }
}

