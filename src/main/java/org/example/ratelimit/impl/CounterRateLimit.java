package org.example.ratelimit.impl;

import org.example.ratelimit.RateLimit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CounterRateLimit implements RateLimit {

    private final long limitTimes;
    private final long refreshTime;
    private long currentWindowStartTime;
    private volatile AtomicLong count;

    /**
     * @param times    次数
     * @param time     时间 时间窗口
     * @param timeUnit 单位
     * @param timeUnit
     */
    public CounterRateLimit(long times, long time, TimeUnit timeUnit) {
        this.limitTimes = times;
        this.refreshTime = TimeUnit.MILLISECONDS.convert(time, timeUnit);
        this.currentWindowStartTime = System.currentTimeMillis();
        this.count = new AtomicLong(0);
    }

    public CounterRateLimit(long times, long time) {
        this.limitTimes = times;
        this.refreshTime = TimeUnit.MILLISECONDS.convert(time, TimeUnit.MILLISECONDS);
        this.currentWindowStartTime = System.currentTimeMillis();
        this.count = new AtomicLong(0);
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        if (now > currentWindowStartTime + refreshTime) {
            this.currentWindowStartTime = this.currentWindowStartTime + refreshTime;
            this.count = new AtomicLong(0);
            return false;
        } else {
            this.count.incrementAndGet();
            return true;
        }
    }
}
