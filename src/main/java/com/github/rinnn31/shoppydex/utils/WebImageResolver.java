package com.github.rinnn31.shoppydex.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Path;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;

public class WebImageResolver {
    @Value("${app.product.image.upload-dir}")
    private static String IMAGE_BASE_DIR;

    public static String pushImage(String imageName, byte[] data) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            if (image == null) {
                return null;
            }
            String outputPath = Path.of(IMAGE_BASE_DIR, imageName + ".png").toString();
            File output = new File(outputPath);
            if (output.getParentFile() != null) {
                output.getParentFile().mkdirs();
            }
            ImageIO.write(image, "png", output);
            return outputPath;

        } catch (java.io.IOException e) {
            return null;
        }
    }

    public static String pushImage(String path, String base64Data) {
        byte[] data = Base64.getDecoder().decode(base64Data);
        return pushImage(path, data);
    }

    public static void deleteImage(String imageName) {
        String imagePath = Path.of(IMAGE_BASE_DIR, imageName + ".png").toString();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }
}
