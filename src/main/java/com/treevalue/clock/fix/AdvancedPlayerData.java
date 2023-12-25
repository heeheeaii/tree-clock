package com.treevalue.clock.fix;

import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.util.concurrent.Future;

//data
public class AdvancedPlayerData {
    public static boolean hasCloseSimplePlayer;
    public static int songTotalLength;

    static public BufferedInputStream bis;

    static public String filePath;

    static public Future<?> future;

    static public AdvancedPlayer advancedPlayer;
}
