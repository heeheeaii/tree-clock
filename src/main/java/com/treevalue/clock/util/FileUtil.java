package com.treevalue.clock.util;

public class FileUtil {
    public static String removeFileExtension(String fileName) {
        // 找到最后一个点（.）的位置
        int extensionIndex = fileName.lastIndexOf(".");
        // 如果确实存在点，且不在字符串开头
        if (extensionIndex > 0) {
            return fileName.substring(0, extensionIndex);
        }
        // 没有扩展名或其他情况，返回原文件名
        return fileName;
    }

}
