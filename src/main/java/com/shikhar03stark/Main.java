package com.shikhar03stark;

import com.shikhar03stark.model.JobInfo;
import com.shikhar03stark.scheduler.Executor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final List<JobInfo> jobs = getSleepJobs(16);

        final Executor jobExec = new Executor(8);
        jobExec.add(jobs);

        System.out.println("main");
        jobExec.start();
        System.out.println("back in main");
        jobExec.blockTillAllDone();
        System.out.println("bye from main");
    }

    private static JobInfo sleepJob(String name, Duration d) {
        return new JobInfo(() -> {
            try {
                Thread.sleep(d);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("Hello from " + name);
        });
    }

    private static List<JobInfo> getSleepJobs(int count) {
        final List<JobInfo> jobs = new ArrayList<>();
        for(int i = 0; i<count; i++) {
            jobs.add(sleepJob(String.valueOf(i), Duration.ofSeconds(2)));
        }
        return jobs;
    }
}