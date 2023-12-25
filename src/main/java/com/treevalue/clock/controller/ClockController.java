package com.treevalue.clock.controller;

import com.treevalue.clock.fix.AdvancedPlayerData;
import com.treevalue.clock.func.Clock;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.sound.sampled.*;

public class ClockController {

    private Clock alarmClock = Clock.getSingleton();

    @FXML
    private TextField alarmTime;

    @FXML
    private Label timeDisplay;

    @FXML
    private ComboBox<Integer> timerOptions;

    @FXML
    private ComboBox<String> ringtoneOptions;

    @FXML
    private Button playBt;
    @FXML
    private Button stopBt;
    @FXML
    private Button ensureBt;

    /**
     * cancel button
     */
    @FXML
    private Button CclBt;

    @FXML
    private Slider durationSlider;

    /**
     * duration label, ring rings time
     */
    @FXML
    private Label dratLb;

    private Timeline clock;

    public void initialize() {
        setupClock();
        initializeTimerOptions();
        initializeRingtoneOptions();
        initializeSlider();
        initializeLable();
        setupAlarmTimeValidation();
        setupPlayStopButtons();
    }

    private void setupClock() {
        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateClock()),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        timeDisplay.setText(sdf.format(new Date()));
    }


    private void initializeTimerOptions() {
        ObservableList<Integer> timerOptionsList = FXCollections.observableArrayList(7, 17, 30);
        timerOptions.setItems(timerOptionsList);
    }

    private void initializeRingtoneOptions() {
        // Add song names or file paths here
        ObservableList<String> ringtoneOptionsList = FXCollections.observableArrayList();
        ringtoneOptionsList.addAll(getAudioFileNames(alarmClock.MUSIC_PATH));
        ringtoneOptions.setItems(ringtoneOptionsList);
        if (ringtoneOptionsList.size() != 0) {
            ringtoneOptions.setPromptText(ringtoneOptionsList.get(0));
            AdvancedPlayerData.filePath = ringtoneOptionsList.get(0);
        }
    }

    @Deprecated
    // should form clock to get file names
    private List<String> getAudioFileNames(String directoryPath) {
        ArrayList<String> audioFileNames = new ArrayList<>();
        File directory = new File(directoryPath);

        // 创建一个过滤器，只接受扩展名为.wav或.mp3的文件
        FilenameFilter audioFilter = (dir, name) -> name.toLowerCase().endsWith(".wav") || name.toLowerCase().endsWith(".mp3");

        // 列出所有满足过滤器条件的文件
        File[] files = directory.listFiles(audioFilter);

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    audioFileNames.add(removeFileExtension(file.getName()));
                }
            }
        }
        return audioFileNames;
    }


    private static String removeFileExtension(String fileName) {
        // 找到最后一个点（.）的位置
        int extensionIndex = fileName.lastIndexOf(".");
        // 如果确实存在点，且不在字符串开头
        if (extensionIndex > 0) {
            return fileName.substring(0, extensionIndex);
        }
        // 没有扩展名或其他情况，返回原文件名
        return fileName;
    }

    private void initializeSlider() {
        // 设置滑块的范围和初始值
        durationSlider.setMin(0);
        durationSlider.setMax(100);
        durationSlider.setValue(50);

        // 添加监听器
        durationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                dratLb.setText(String.valueOf(newValue.intValue()));
                alarmClock.changePosition(newValue.intValue());
            }
        });
    }

    private void initializeLable() {
        dratLb.setText(String.valueOf((int) durationSlider.getValue()));
    }

    private void setupAlarmTimeValidation() {
        alarmTime.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidTimeFormat(newValue)) {
                alarmTime.setStyle("-fx-text-fill: red;");
            } else {
                alarmTime.setStyle("-fx-text-fill: black;");
            }
        });
    }

    private boolean isValidTimeFormat(String time) {
        String regex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
        return Pattern.matches(regex, time);
    }

    private void setupRingtoneOptions() {
        ringtoneOptions.getSelectionModel().selectedItemProperty().addListener(
                (options, oldValue, newValue) -> {
                    String path = alarmClock.mp3nameToPath(ringtoneOptions.getValue());
                    AdvancedPlayerData.filePath = path;
                }
        );
    }

    private void setupPlayStopButtons() {
        playBt.setOnAction(e -> playSelectedSong());
        stopBt.setOnAction(e -> stopPlayingSong());
    }

    private void playSelectedSong() {
        String selectedSong = ringtoneOptions.getValue();
        if (selectedSong == null) {
            selectedSong = ringtoneOptions.getPromptText();
        }
        alarmClock.playMp3(selectedSong);
    }

    private void stopPlayingSong() {
        alarmClock.stopPlayMusic();
    }


}
