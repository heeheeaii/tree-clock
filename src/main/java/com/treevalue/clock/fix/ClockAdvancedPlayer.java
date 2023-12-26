package com.treevalue.clock.fix;

import com.treevalue.clock.data.ClockAlarmData;
import com.treevalue.clock.thread.ClockThreadPool;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

//data
public class ClockAdvancedPlayer {
    public static boolean hasCloseSimplePlayer;

    static public BufferedInputStream bis;

    static public String filePath;

    static public Future<?> future;

    static public AdvancedPlayer advancedPlayer;

    static public boolean isTryPlay;

    static public int percentage;

    static public ClockAlarmData clockAlarmData;

    public static void closeAdPlayer() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
            future = null;
        }
        if (advancedPlayer != null) {
            advancedPlayer.close();
            advancedPlayer = null;
        }
        if (bis != null) {
            try {
                bis.close();
                bis = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static long getSongTotalLength() {
        if (bis == null) {
            return 0;
        }
        try {
            return bis.available();
        } catch (IOException e) {
            return 0;
        }
    }

    public static long getPercentPosition(int num) {
        if (num < 0 || num > 100) {
            throw new IllegalArgumentException("outPercentage must be between 0 and 100");
        }
        return (getSongTotalLength() * num) / 100;

    }

    static public void changePosition(int outPercentage) {
        if (outPercentage < 0 || outPercentage > 100) {
            throw new IllegalArgumentException("outPercentage must be between 0 and 100");
        }
        if (filePath == null) {
            return;
        }
        closeAdPlayer();
        try {
            long pauseLocation;
            bis = new BufferedInputStream(new FileInputStream(filePath));
            pauseLocation = (getSongTotalLength() * outPercentage) / 100;
            if (!isTryPlay) {
                return;
            }
            bis.skip(pauseLocation);
            advancedPlayer = new AdvancedPlayer(bis);
            future = ClockThreadPool.pool.submit(() -> {
                try {
                    advancedPlayer.play();
                    bis.close();
                    bis = null;
                    advancedPlayer.close();
                    advancedPlayer = null;
                    future = null;
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    static private void startAlarm(AtomicBoolean finish) {
        closeAdPlayer();
        try {
            bis = new BufferedInputStream(new FileInputStream(clockAlarmData.filePath));
            bis.skip(clockAlarmData.beginPosition);
            advancedPlayer = new AdvancedPlayer(bis);
            future = ClockThreadPool.pool.submit(() -> {
                try {
                    advancedPlayer.play();
                    closeAdPlayer();
                    finish.set(true);
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    static private Boolean timeIn(LocalTime beginTime, int waitSeconds) {
        LocalTime var = LocalTime.now();
        return (var.getHour() - beginTime.getHour()) * 3600 + (var.getMinute() - beginTime.getMinute()) * 60 + (var.getSecond() - beginTime.getSecond()) <= waitSeconds;
    }

    static public Future<?> startAlarmThread() {
        return ClockThreadPool.pool.submit(() -> {
            LocalTime aMoment;
            while (clockAlarmData.allowRun) {
                aMoment = LocalTime.now();
                if (aMoment.getHour() == clockAlarmData.hour && aMoment.getMinute() == clockAlarmData.minute
                        && Math.abs(aMoment.getSecond() - clockAlarmData.second) <= 3) {
                    LocalTime begingTime = LocalTime.now();
                    AtomicBoolean finishOnce = new AtomicBoolean(false);
                    while (clockAlarmData.allowRun && timeIn(begingTime, 900)) {
                        startAlarm(finishOnce);
                        while (!finishOnce.get() && clockAlarmData.allowRun && timeIn(begingTime, 900)) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        finishOnce.set(false);
                    }
                    clockAlarmData.allowRun = false;
                    return;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
