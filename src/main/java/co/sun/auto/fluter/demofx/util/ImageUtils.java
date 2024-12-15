package co.sun.auto.fluter.demofx.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    // Function to resize an image
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    // Function to convert an image file to byte array with compression
    public static byte[] convertImageToByteArray(String imagePath, String format) throws IOException {
        float quality = 0.25f; // Set the desired compression quality (0.0 to 1.0)
        int maxWidth = 600; // Maximum width to resize image (if needed)
        int maxHeight = 400; // Maximum height to resize image (if needed)
        // Read the image from the specified path
        BufferedImage image = ImageIO.read(new File(imagePath));
        if (image == null) {
            throw new IOException("Invalid image file: " + imagePath);
        }

        // Resize the image if necessary (to reduce size)
        if (image.getWidth() > maxWidth || image.getHeight() > maxHeight) {
            image = resizeImage(image, maxWidth, maxHeight);
        }

        // Compress the image if format is JPG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if ("JPG".equalsIgnoreCase(format) || "JPEG".equalsIgnoreCase(format) || "PNG".equalsIgnoreCase(format)) {
            // Use ImageWriter for better control over image compression
            ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality); // Set the quality (from 0 to 1)
            writer.setOutput(ImageIO.createImageOutputStream(baos));
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        } else {
            // If not JPG, just write the image directly (no compression)
            ImageIO.write(image, format, baos);
        }

        return baos.toByteArray(); // Return the byte array
    }
}
