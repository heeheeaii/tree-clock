package com.treevalue.clock.controller;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AlarmClockController {

    @FXML
    private TextField alarmTime;

    @FXML
    private ComboBox<Integer> timerOptions;

    @FXML
    private ComboBox<Integer> durationOptions;

    @FXML
    private void initialize() {
        // 初始化代码，比如填充durationOptions
        for (int i = 0; i <= 15; i++) {
            durationOptions.getItems().add(i);
        }
    }

    @FXML
    private void handleSetTimeAction() {
        // 处理设置时间的逻辑
    }

    // 其他事件处理方法...
}
