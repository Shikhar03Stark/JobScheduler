package com.shikhar03stark.scheduler;

import com.shikhar03stark.model.JobInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Executor {
    private final Queue<JobInfo> jobQueue = new ConcurrentLinkedQueue<>();
    private final int totalWorkers;
    private boolean isRunning;
    private Thread executorThread;
    private final Semaphore jobDoneMutex = new Semaphore(1);
    private int jobDoneCount = 0;
    private final Semaphore allJobDoneMutex = new Semaphore(1);

    public Executor(int totalWorkers) {
        this.totalWorkers = totalWorkers;
        this.isRunning = false;
    }

    public void add(JobInfo jobInfo) {
        jobQueue.add(jobInfo);
    }

    public void add(List<JobInfo> jobs) {
        jobQueue.addAll(jobs);
    }

    private void executorThreadTask() {
        for(int t = 0; t<totalWorkers; t++){
            new Thread(() -> {
                while (!jobQueue.isEmpty()){
                    final JobInfo jobInfo = jobQueue.poll();
                    try {
                        jobInfo.getAction().action();
                    } catch (Exception e) {
                        jobInfo.setLastCapturedException(e);
                    } finally {
                        jobInfo.setExecuteCounter(jobInfo.getExecuteCounter()+1);
                        jobInfo.setLastExecuted(LocalDateTime.now());
                    }
                }
                try {
                    jobDoneMutex.acquire();
                    jobDoneCount++;
                    if(jobDoneCount == totalWorkers) {
                        allJobDoneMutex.release();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    jobDoneMutex.release();
                }
            }).start();
        }
    }

    public void start() {
        if (isRunning) return;
        executorThread = new Thread(this::executorThreadTask);
        executorThread.start();
        isRunning = true;
        try {
            allJobDoneMutex.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void blockTillAllDone() {
        if(!isRunning) throw new RuntimeException("Executor not running");
        try {
            allJobDoneMutex.acquire();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            allJobDoneMutex.release();
        }
    }

    public Thread getExecutorThread() {
        return executorThread;
    }
}
