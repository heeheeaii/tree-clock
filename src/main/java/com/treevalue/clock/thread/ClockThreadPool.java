package com.treevalue.clock.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClockThreadPool {
    @SuppressWarnings("AlibabaThreadPoolCreation")
    public static ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();

}
