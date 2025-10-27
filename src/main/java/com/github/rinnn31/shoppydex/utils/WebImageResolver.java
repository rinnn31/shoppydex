package com.github.rinnn31.shoppydex.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class WebImageResolver {
    private static final String IMAGE_BASE_DIR="/images/";

    public static void writeImage(String imageName, byte[] data) {
        try {
            String fullPath=IMAGE_BASE_DIR+imageName+".png";
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            if (image == null) {
                return;
            }
            // Write always as PNG
            File output = new File(fullPath);

            ImageIO.write(image, "png", output);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeImage(String path, String base64Data) {
        byte[] data = Base64.getDecoder().decode(base64Data);
        writeImage(path, data);
    }
}
