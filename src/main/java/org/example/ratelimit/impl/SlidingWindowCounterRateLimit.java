package org.example.ratelimit.impl;

import org.example.ratelimit.RateLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class SlidingWindowCounterRateLimit implements RateLimit {

    private final long limitTimes;
    private final long windowSize; // ms
    private final int windowSplitNum;
    private long currentWindowStartTime;
    private List<AtomicLong> counts;
    private int startIndex;

    public SlidingWindowCounterRateLimit(long limitTimes, long windowSize, int windowSplitNum) {
        this.limitTimes = limitTimes;
        this.windowSize = windowSize;
        this.windowSplitNum = windowSplitNum;
        this.currentWindowStartTime = System.currentTimeMillis();
        this.counts = new ArrayList<>(windowSplitNum);
        this.startIndex = 0;
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        int slideWindowsNum = (int) (Math.max(now - windowSize - currentWindowStartTime, 0) / (windowSize / windowSplitNum));
        long cnt = 0;
        for (AtomicLong i : this.counts) cnt += i.get();
        if (cnt > limitTimes) {
            return false;
        } else {
            this.counts.get(this.startIndex).incrementAndGet();
            return true;
        }
    }

    private void slideWindows(int slideWindowsNum) {
        if (slideWindowsNum <= 0) return;
        slideWindowsNum = Math.min(slideWindowsNum, this.windowSplitNum);
        for (int i = 0; i < this.windowSplitNum; ++i) {
            this.startIndex = (this.startIndex + 1) % this.windowSplitNum;
            this.counts.set(this.startIndex, new AtomicLong(0));
        }
        this.currentWindowStartTime = this.currentWindowStartTime + windowSplitNum * (windowSize / windowSplitNum);
    }

}
