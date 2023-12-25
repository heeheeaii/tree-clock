package com.treevalue.clock.func;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Alarm alarm = new Alarm("C:\\Users\\wyhee\\Downloads\\111\\clock\\src\\main\\resources\\music");
        alarm.setVolume(50); // Set volume to 50%
        alarm.setAlarm("我在那一角落患过伤风.mp3", 0, 60000, TimeUnit.SECONDS); // Play every minute, starting immediately
    }
}
