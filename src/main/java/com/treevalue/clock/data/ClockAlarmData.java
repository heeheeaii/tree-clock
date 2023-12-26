package com.treevalue.clock.data;

public class ClockAlarmData {
    public int hour;
    public int minute;
    public int second;
    public String filePath;
    public long beginPosition;

    public boolean allowRun;


    public ClockAlarmData(int hour, int minute, int second, String mp3Path, long beginPosition, boolean allowRun) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.filePath = mp3Path;
        this.beginPosition = beginPosition;
        this.allowRun = allowRun;
    }
}
