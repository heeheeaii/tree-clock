package com.treevalue.clock.controller;

import com.treevalue.clock.data.ClockAlarmData;
import com.treevalue.clock.fix.ClockAdvancedPlayer;
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
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

import static com.treevalue.clock.fix.ClockAdvancedPlayer.bis;

public class ClockController {

    private Clock alarmClock = Clock.getSingleton();

    @FXML
    private Button tryBt;


    @FXML
    private TextField alarmTime;

    @FXML
    private Label timeDisplay;

    @FXML
    private ComboBox<String> timerOptions;

    @FXML
    private ComboBox<String> ringtoneOptions;

    @FXML
    private Button playBt;

    @FXML
    private Button ensureBt;

    /**
     * cancel button
     */
    @FXML
    private Button stopAlarmBt;

    @FXML
    private Slider durationSlider;

    /**
     * duration label, ring rings time
     */
    @FXML
    private Label dratLb;

    private Timeline clock;

    public void initialize() {
        initializeClock();
        initializeTimerOptions();
        initializeRingtoneOptions();
        initializeSlider();
        initializeLable();
        initializeAlarmTimeValidation();
        initializePlayStopButtons();
        initializeTryButton();
        initializeEnsureButton();
        initializeCclButton();
    }

    private void initializeCclButton() {
        stopAlarmBt.setOnAction(e -> {
            alarmTime.setEditable(true);
            if (ClockAdvancedPlayer.clockAlarmData != null) {
                ClockAdvancedPlayer.clockAlarmData.allowRun = false;
            }
            ClockAdvancedPlayer.closeAdPlayer();
        });
    }

    private void initializeEnsureButton() {
        ensureBt.setOnAction(e -> {
            if (!isValidTimeFormat(alarmTime.getText())) {
                alarmTimeColorSet(isValidTimeFormat(alarmTime.getText()));
                return;
            }
            if (ringtoneOptions.getValue() == null) {
                return;
            }
            alarmTime.setEditable(false);
            String[] stringTime = alarmTime.getText().split(":");
            ClockAdvancedPlayer.clockAlarmData = new ClockAlarmData(Integer.valueOf(stringTime[0]), Integer.valueOf(stringTime[1]), Integer.valueOf(stringTime[2]), alarmClock.mp3nameToPath(ringtoneOptions.getValue()), ClockAdvancedPlayer.getPercentPosition((int) durationSlider.getValue()), true);
            ClockAdvancedPlayer.startAlarmThread();
        });
    }


    private void updateTryBt() {
        String tryStr = "Try";
        String closeStr = "Close";
        if (tryStr.equals(tryBt.getText())) {
            tryBt.setText(closeStr);
            ClockAdvancedPlayer.isTryPlay = true;
            ClockAdvancedPlayer.filePath = alarmClock.mp3nameToPath(ringtoneOptions.getValue());
            ClockAdvancedPlayer.changePosition((int) durationSlider.getValue());
        } else {
            tryBt.setText(tryStr);
            ClockAdvancedPlayer.closeAdPlayer();
            ClockAdvancedPlayer.isTryPlay = false;
        }
    }

    private void initializeTryButton() {
        tryBt.setText("Try");
        tryBt.setOnAction(e -> updateTryBt());
    }


    private void initializeClock() {
        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateClock()), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        timeDisplay.setText(sdf.format(new Date()));
    }

    private void timerOptionsSetAlarmTime(String timerOptionsText) {
//       HH:mm:ss
        LocalTime aMoment = LocalTime.now();
        int hours, minutes, seconds;
        minutes = timerOptionValueToInt(timerOptionsText);
        minutes += aMoment.getMinute();
        hours = (aMoment.getHour() + (minutes) / 60) % 24;
        minutes %= 60;
        seconds = aMoment.getSecond();
        StringBuilder sb = new StringBuilder();
        if (hours < 10) {
            sb.append("0");
        }
        sb.append(hours).append(":");
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes).append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        alarmTime.setText(sb.toString());
    }

    private void initializeTimerOptions() {
        ObservableList<String> timerOptionsList = FXCollections.observableArrayList("7 min", "17 min", "30 min");
        timerOptions.setItems(timerOptionsList);
        if (!timerOptionsList.isEmpty()) {
            timerOptions.setValue(timerOptionsList.get(0));
        }
        timerOptionsSetAlarmTime(timerOptions.getValue());
        timerOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            timerOptionsSetAlarmTime(newValue);
        });
    }

    private int timerOptionValueToInt(String timerOp) {
        // newValue must > 0
        return Math.abs(Integer.valueOf(timerOp.split(" ")[0]));
    }

    private void initializeRingtoneOptions() {
        // Add song names or file paths here
        ObservableList<String> ringtoneOptionsList = FXCollections.observableArrayList();
        ringtoneOptionsList.addAll(getAudioFileNames(alarmClock.MUSIC_PATH));
        ringtoneOptions.setItems(ringtoneOptionsList);
        if (ringtoneOptionsList.size() != 0) {
            ringtoneOptions.setValue(ringtoneOptionsList.get(0));
            ClockAdvancedPlayer.filePath = alarmClock.mp3nameToPath(ringtoneOptionsList.get(0));
        }
        ringtoneOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String path = alarmClock.mp3nameToPath(ringtoneOptions.getValue());
            ClockAdvancedPlayer.filePath = path;
        });
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

    /**
     * advancePlayer
     */
    private void tryPlayAdPlayer() {
        if (ClockAdvancedPlayer.isTryPlay) {
            stopAdPlayer();
            if (ClockAdvancedPlayer.filePath == null) {
                return;
            }
            try {
                bis = new BufferedInputStream(new FileInputStream(ClockAdvancedPlayer.filePath));
                ClockAdvancedPlayer.advancedPlayer = new AdvancedPlayer(ClockAdvancedPlayer.bis);
                ClockAdvancedPlayer.advancedPlayer.play(ClockAdvancedPlayer.percentage);
            } catch (JavaLayerException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void initializeSlider() {
        // 设置滑块的范围和初始值
        durationSlider.setMin(0);
        durationSlider.setMax(100);
        durationSlider.setValue(0);

        ClockAdvancedPlayer.filePath = alarmClock.mp3nameToPath(ringtoneOptions.getValue());
        // 添加监听器
        durationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                durationSlider.setValue((double) newValue);
                dratLb.setText(String.valueOf(newValue.intValue()));
                if (ClockAdvancedPlayer.isTryPlay) {
                    if (ClockAdvancedPlayer.filePath == null) {
                        ClockAdvancedPlayer.filePath = alarmClock.mp3nameToPath(ringtoneOptions.getValue());
                    }
                    ClockAdvancedPlayer.changePosition(newValue.intValue());
                }
            }
        });
    }

    private void initializeLable() {
        dratLb.setText(String.valueOf((int) durationSlider.getValue()));
    }

    private void alarmTimeColorSet(Boolean timeTextRight) {
        if (!timeTextRight) {
            alarmTime.setStyle("-fx-text-fill: red;");
        } else {
            alarmTime.setStyle("-fx-text-fill: #2E8B57;");
        }
    }

    private void initializeAlarmTimeValidation() {
        alarmTime.textProperty().addListener((observable, oldValue, newValue) -> {
            alarmTimeColorSet(isValidTimeFormat(newValue));
        });
    }

    private boolean isValidTimeFormat(String time) {
        String regex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
        return Pattern.matches(regex, time);
    }

    private void initializePlayStopButtons() {
        playBt.setOnAction(e -> whenPlayButtonChange());
    }

    private void whenPlayButtonChange() {
        var playStr = "Play";
        var stopStr = "Stop";
        if (playStr.equals(playBt.getText())) {
            playBt.setText(stopStr);
            playSelectedSong();
        } else {
            playBt.setText(playStr);
            stopPlayingSong();
        }
    }

    private void stopAdPlayer() {
        if (ClockAdvancedPlayer.future != null && !ClockAdvancedPlayer.future.isCancelled()) {
            ClockAdvancedPlayer.future.cancel(true);
        }
        if (ClockAdvancedPlayer.advancedPlayer != null) {
            ClockAdvancedPlayer.advancedPlayer.close();
        }
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void playSelectedSong() {
        stopAdPlayer();
        String selectedSong = ringtoneOptions.getValue();
        if (selectedSong == null) {
            selectedSong = ringtoneOptions.getPromptText();
        }
        alarmClock.playMp3(selectedSong);
    }

    private void stopPlayingSong() {
        stopAdPlayer();
        alarmClock.stopPlayMusic();
    }
}
