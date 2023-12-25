package com.treevalue.clock;

import com.treevalue.clock.thread.ClockThreadPool;
import javafx.concurrent.Task;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.sound.sampled.FloatControl;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class MP3PlayerExample {

    public static void f() {
        {
            String filePath = "C:\\Users\\wyhee\\Downloads\\111\\clock\\src\\main\\resources\\music\\我在那一角落患过伤风.mp3"; // 替换为你的MP3文件路径

            try {
                FileInputStream fis = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Player player = new Player(bis);

                System.out.println("播放音乐: " + filePath);

                player.play(); // 开始播放

                // 如果你想在音乐播放完毕后关闭播放器，可以添加以下代码
                player.close();
                bis.close();
                fis.close();
            } catch (Exception e) {
                System.err.println("播放MP3音乐时出错: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        f();
    }
}
