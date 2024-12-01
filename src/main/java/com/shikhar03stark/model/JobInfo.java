package com.shikhar03stark.model;

import java.time.LocalDateTime;
import java.util.Random;

public class JobInfo {
    private final long jobId;
    private final Action action;
    private long executeCounter;
    private LocalDateTime lastExecuted;
    private Exception lastCapturedException;

    public JobInfo(Action action) {
        jobId = new Random().nextLong();
        this.action = action;
        executeCounter = 0;
        lastExecuted = null;
        lastCapturedException = null;
    }

    public long getJobId() {
        return jobId;
    }

    public Action getAction() {
        return action;
    }

    public long getExecuteCounter() {
        return executeCounter;
    }

    public LocalDateTime getLastExecuted() {
        return lastExecuted;
    }

    public Exception getLastCapturedException() {
        return lastCapturedException;
    }

    public void setExecuteCounter(long executeCounter) {
        this.executeCounter = executeCounter;
    }

    public void setLastExecuted(LocalDateTime lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    public void setLastCapturedException(Exception lastCapturedException) {
        this.lastCapturedException = lastCapturedException;
    }
}
