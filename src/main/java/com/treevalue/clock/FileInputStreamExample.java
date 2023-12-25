package com.treevalue.clock;

import java.io.FileInputStream;
import java.io.IOException;

public class FileInputStreamExample {
    public static void main(String[] args) {
        try (FileInputStream a = new FileInputStream("path/to/your/file")) {
            FileInputStream b = a;
            // 使用 a 和 b 进行操作
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
