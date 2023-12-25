package com.treevalue.clock.func;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// Import the additional libraries for MP3 support
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Music {
    private Clip clip;
    private Player player; // For MP3 files
    private FloatControl volumeControl;
    private String filePath;

    public void loadMusic(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.filePath = filePath;
        File musicFile = new File(filePath);

        if (filePath.toLowerCase().endsWith(".mp3")) {
            // Use JLayer for MP3 files
            try {
                FileInputStream fis = new FileInputStream(musicFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                player = new Player(bis);
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        } else {
            // Use the standard Java Sound API for other file types
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
    }

    public void play() {
        try {
            if (filePath.toLowerCase().endsWith(".mp3") && player != null) {
                new Thread(() -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if (clip != null) {
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (filePath.toLowerCase().endsWith(".mp3") && player != null) {
            player.close();
        } else if (clip != null) {
            clip.stop();
        }
    }

    public void setVolume(float volume) { // Volume as a percentage
        if (volumeControl != null) {
            float range = volumeControl.getMaximum() - volumeControl.getMinimum();
            float gain = (range * volume) / 100f + volumeControl.getMinimum();
            volumeControl.setValue(gain);
        }
    }
}
