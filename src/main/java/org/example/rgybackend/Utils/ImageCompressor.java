package org.example.rgybackend.Utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import net.coobird.thumbnailator.Thumbnails;

@Component
public class ImageCompressor {

    public String compressBase64Image(String base64Image) throws IOException {
        String imageData = base64Image;
        String prefix = null;
        if(base64Image.contains(",")) {
            prefix = base64Image.split(",")[0] + ",";
            imageData = base64Image.split(",")[1];
        }

        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        final int targetSizeKB = 100;
        final int targetSize = targetSizeKB * 1024;

        float quality = 0.9f;
        int width = 1200;
        int height = 1200;
        byte[] compressedBytes = imageBytes;

        while(compressedBytes.length > targetSize && (quality > 0.1f || width > 100)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(originalImage)
                    .size(width, height)
                    .outputQuality(quality)
                    .outputFormat("jpeg")
                    .toOutputStream(outputStream);

            compressedBytes = outputStream.toByteArray();

            if(compressedBytes.length > targetSize) {
                quality -= 0.1f;
                if(quality < 0.1f) {
                    quality = 0.1f;
                    width = (int)(width * 0.9);
                    height = (int)(height * 0.9);
                }
            }
        }
        
        System.out.println("图片压缩完成: quality: " + quality + ", width/height: " + width + ", size: " + compressedBytes.length / 1024 + "KB");

        String compressedImg = Base64.getEncoder().encodeToString(compressedBytes);
        return prefix == null ? compressedImg : prefix + compressedImg;
    }
}