package com.treevalue.clock;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.decoder.JavaLayerException;

import java.io.FileInputStream;
import java.io.IOException;

public class MP3Player3 {
    private final String filePath;
    private AdvancedPlayer player;
    private Thread playerThread;
    private long songTotalLength;
    private long pauseLocation;
    private FileInputStream fileInputStream;

    public MP3Player3(String filePath) {
        this.filePath = filePath;
    }

    public void play() throws JavaLayerException, IOException {
        fileInputStream = new FileInputStream(filePath);
        songTotalLength = fileInputStream.available();
        player = new AdvancedPlayer(fileInputStream);

        playerThread = new Thread(() -> {
            try {
                player.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        });

        playerThread.start();
    }

    public void changePosition(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        if (player != null) {
            try {
                pauseLocation = (songTotalLength * percentage) / 100;
                fileInputStream.close();
                fileInputStream = new FileInputStream(filePath);
                fileInputStream.skip(pauseLocation);
                if (playerThread != null && playerThread.isAlive()) {
                    player.close();
                    playerThread.interrupt();
                }
                player = new AdvancedPlayer(fileInputStream);
                playerThread = new Thread(() -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                });
                playerThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            MP3Player3 mp3Player = new MP3Player3("C:\\Users\\wyhee\\Downloads\\111\\clock\\src\\main\\resources\\music\\我在那一角落患过伤风.mp3");
            mp3Player.play();

            // 等待一段时间后改变播放位置
            Thread.sleep(5000); // 延时5000ms
            mp3Player.changePosition(50); // 改变到50%的位置
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
