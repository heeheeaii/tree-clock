package com.treevalue.clock.func;

import com.treevalue.clock.fix.AdvancedPlayerData;
import com.treevalue.clock.thread.ClockThreadPool;
import com.treevalue.clock.util.FileUtil;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * @author hee
 */
public class Clock {

    private int hours;
    private int minutes;
    private int seconds;
    private int timer;

    private String ringtone;

    private int volume;

    private boolean ensure;
    private boolean ring;

    static Clock singleInstance;

    public final String MUSIC_PATH = "src/main/resources/music/";

    public HashMap<String, String> music;

    private volatile Player player = null;

    private volatile BufferedInputStream bis = null;

    public Clock() {
        init();
    }

    void init() {
        loadMusicName();
    }

    private void loadMusicName() {
        this.music = new HashMap<>(8);
        File directory = new File(this.MUSIC_PATH);

        // 创建一个过滤器，只接受扩展名为.wav或.mp3的文件
        FilenameFilter audioFilter = (dir, name) -> name.toLowerCase().endsWith(".wav") || name.toLowerCase().endsWith(".mp3");

        // 列出所有满足过滤器条件的文件
        File[] files = directory.listFiles(audioFilter);

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    this.music.put(FileUtil.removeFileExtension(file.getName()), file.getName());
                }
            }
        }

    }

    public String getpathAfterResources(String path) {
        int index = path.indexOf("resources");
        if (index != -1) {
            return path.substring(index + "resources".length());
        } else {
            return path;
        }
    }

    public String mp3nameToPath(String mp3name) {
        URL audioFileUrl = Clock.class.getResource(getpathAfterResources(this.MUSIC_PATH) + music.get(mp3name));
        if (audioFileUrl == null) {
            return null;
        }
        return audioFileUrl.getPath();
    }

    public void playMp3(String mp3name) {
        String absolutePath = mp3nameToPath(mp3name);
        try {
            bis = new BufferedInputStream(new FileInputStream(absolutePath));
            player = new Player(bis);
//            if(AdvancedPlayerData.future!=null){
//                if(!AdvancedPlayerData.future.isDone()){
//                    AdvancedPlayerData.future.cancel(true);
//                }
//            }
            AdvancedPlayerData.future = ClockThreadPool.pool.submit(
                    () -> {
                        try {
                            player.play();
                        } catch (JavaLayerException e) {
                            throw new RuntimeException(e);
                        }
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
            );
        } catch (FileNotFoundException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }


    public void stopPlayMusic() {
        if (player != null) {
            player.close();
            player = null;
        }
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bis = null;
        }
    }

    private void closePlayer() {
        if (player != null) {
            player.close();
            player = null;
        }
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bis = null;
        }
        AdvancedPlayerData.hasCloseSimplePlayer = true;
    }

    public void changePosition(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        if (!AdvancedPlayerData.hasCloseSimplePlayer) {
            closePlayer();
        }
        long pauseLocation;
        try {
            pauseLocation = (AdvancedPlayerData.songTotalLength * percentage) / 100;
            AdvancedPlayerData.bis = new BufferedInputStream(new FileInputStream(AdvancedPlayerData.filePath));
            AdvancedPlayerData.bis.skip(pauseLocation);
            if (AdvancedPlayerData.future != null && !AdvancedPlayerData.future.isDone()) {
                AdvancedPlayerData.future.cancel(true);
                AdvancedPlayerData.advancedPlayer.close();
            }
            AdvancedPlayerData.advancedPlayer = new AdvancedPlayer(AdvancedPlayerData.bis);
            AdvancedPlayerData.future = ClockThreadPool.pool.submit(() -> {
                try {
                    AdvancedPlayerData.advancedPlayer.play();
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

    private static class SgltHolder {
        private static final Clock INSTANCE = new Clock();
    }

    public static Clock getSingleton() {
        return SgltHolder.INSTANCE;
    }

    public void setHours(int hours) {
        int oneDay = 24;
        if (hours >= 0 && hours < oneDay) {
            this.hours = hours;
        }
    }

    public void setMinutes(int minutes) {
        int oneHour = 60;
        if (minutes >= 0 && minutes < oneHour) {
            this.minutes = minutes;
        }
    }

    public void setSeconds(int seconds) {
        int oneMinute = 60;
        if (seconds >= 0 && seconds < oneMinute) {
            this.seconds = seconds;
        }
    }

    public String displayTime() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void main(String[] args) {
        Clock clock = new Clock();

        // Set time to 10:30:25

        URL resource1 = clock.getClass().getResource("/music/" + "我在那一角落患过伤风.mp3");
        URL resource = Clock.class.getResource("/music/" + "我在那一角落患过伤风.mp3");
//        URL resource = Clock.class.getClass();
//        String resourcePath = new Clock().getpathAfterResources("resources/haha.txt");
    }
}
