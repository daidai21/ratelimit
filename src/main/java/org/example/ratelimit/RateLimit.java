package org.example.ratelimit;

public interface RateLimit {

    boolean tryAcquire();
}
