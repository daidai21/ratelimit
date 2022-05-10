package org.example.ratelimit.impl;

import org.example.ratelimit.RateLimit;

/**
 * 存储请求，异步线程释放请求
 */
public class LeakyBucketRateLimit  implements RateLimit {
    @Override
    public synchronized boolean tryAcquire() {
        // TODO
        return false;
    }
}

