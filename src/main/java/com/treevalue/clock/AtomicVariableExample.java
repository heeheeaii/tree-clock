package com.treevalue.clock;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVariableExample {
    // 声明原子变量
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        // 创建并启动第一个线程，负责更新原子变量的值
        Thread updateThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                int currentValue = atomicInteger.incrementAndGet();
                System.out.println("Thread 1: Incremented value to " + currentValue);
                try {
                    Thread.sleep(100); // 稍作延迟以便观察输出
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 创建并启动第二个线程，负责读取原子变量的值
        Thread readThread = new Thread(() -> {
            int lastReadValue = 0;
            while (lastReadValue < 5) {
                int currentValue = atomicInteger.get();
                if (currentValue != lastReadValue) {
                    System.out.println("Thread 2: Detected a new value: " + currentValue);
                    lastReadValue = currentValue;
                }
            }
        });

        // 启动线程
        updateThread.start();
        readThread.start();

        // 等待线程完成
        updateThread.join();
        readThread.join();
    }
}
