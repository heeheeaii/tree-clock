package com.treevalue.clock;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.decoder.JavaLayerException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MP3Player2 {
    private String filePath;
    private AdvancedPlayer player;
    private long pauseLocation;
    private long songTotalLength;
    private FileInputStream fileInputStream;

    public MP3Player2(String filePath) {
        this.filePath = filePath;
    }

    // 播放MP3文件
    public void play(int percentage) {
        try {
            fileInputStream = new FileInputStream(filePath);
            player = new AdvancedPlayer(fileInputStream);
            songTotalLength = fileInputStream.available();

            // 计算要跳转到的位置
            long skipBytes = (songTotalLength * percentage) / 100;
            fileInputStream.skip(skipBytes);

            // 新线程来播放音乐，防止阻塞主线程
            new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MP3Player2 mp3Player = new MP3Player2("C:\\Users\\wyhee\\Downloads\\111\\clock\\src\\main\\resources\\music\\我在那一角落患过伤风.mp3");
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入0-100之间的数字来改变MP3播放位置:");
        int percentage = scanner.nextInt();
        if (percentage < 0 || percentage > 100) {
            System.out.println("请输入一个有效的数字！");
        } else {
            mp3Player.play(percentage);
        }
        scanner.close();
    }
}
