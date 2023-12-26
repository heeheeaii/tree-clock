package com.treevalue.clock.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClockThreadPool {
    @SuppressWarnings("AlibabaThreadPoolCreation")
    public static ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
    Future<?> alarmThread;
}
