package org.example.ratelimit.impl;

import org.example.ratelimit.RateLimit;

public class TokenBucketRateLimit implements RateLimit {
    @Override
    public synchronized boolean tryAcquire() {
        // TODO

        return false;
    }
}
