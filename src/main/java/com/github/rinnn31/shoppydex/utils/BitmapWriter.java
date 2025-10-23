package com.github.rinnn31.shoppydex.utils;

import java.nio.file.Files;
import java.util.Base64;

public class BitmapWriter {
    public static void writeImage(String path, byte[] data) {
        try {
            Files.write(java.nio.file.Paths.get(path), data);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeImage(String path, String base64Data) {
        byte[] data = Base64.getDecoder().decode(base64Data);
        writeImage(path, data);
    }
}
