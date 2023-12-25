package com.treevalue.clock.func;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Alarm {
    private Music music;
    private ScheduledExecutorService timer;
    private String musicDirectory;

    public Alarm(String musicDirectory) {
        this.musicDirectory = musicDirectory;
        this.music = new Music();
        this.timer = new ScheduledThreadPoolExecutor(1);
    }

    public void setMusicDirectory(String musicDirectory) {
        this.musicDirectory = musicDirectory;
    }

    public void setVolume(float volume) {
        music.setVolume(volume);
    }

    public void setAlarm(String musicName, long delay, long period, TimeUnit unit) {
        timer.scheduleAtFixedRate(new TimerTask() {
            private long startTime = -1;

            @Override
            public void run() {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }

                if ((System.currentTimeMillis() - startTime) > 900000) { // 15 minutes
                    music.stop();
                    this.cancel();
                    return;
                }

                try {
                    music.loadMusic(musicDirectory + File.separator + musicName);
                    music.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, period, unit);
    }
}
