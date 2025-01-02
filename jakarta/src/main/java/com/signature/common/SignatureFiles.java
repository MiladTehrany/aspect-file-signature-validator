package com.signature.common;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SignatureFiles {

    private static final long MAX_FILE_SIZE = 5L * 1024L * 1024L; // 5 MB => 5,242,880 bytes

    public byte[] extractFileContent(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteContentStream = new ByteArrayOutputStream();
        ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();

        // Buffer for reading the stream
        byte[] buffer = new byte[8192];
        int bytesRead;
        boolean isFileContent = false;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            if (!isFileContent) {
                // Accumulate header data
                headerBuffer.write(buffer, 0, bytesRead);
                String headers = headerBuffer.toString(StandardCharsets.UTF_8.name());

                // Detect the end of the headers
                int contentStartIndex = headers.indexOf("\r\n\r\n");
                if (contentStartIndex == -1) {
                    contentStartIndex = headers.indexOf("\n\n");
                }

                if (contentStartIndex != -1) {
                    // File content starts immediately after headers
                    isFileContent = true;
                    int fileStartIndex = contentStartIndex + (headers.contains("\r\n\r\n") ? 4 : 2);

                    // Write the file content portion of the buffer
                    byteContentStream.write(headerBuffer.toByteArray(), fileStartIndex, headerBuffer.size() - fileStartIndex);
                    headerBuffer.reset();
                }
            } else {
                byteContentStream.write(buffer, 0, bytesRead);
            }
        }
        byteContentStream.close();
        headerBuffer.close();
        return byteContentStream.toByteArray();
    }

    public void validateImage(InputStream fileInputStream) throws IOException {
        // Read the image
        BufferedImage image = ImageIO.read(fileInputStream);
        if (image == null) {
            throw new IOException("Invalid image file.");
        }
    }

    private boolean isPDF(byte[] signature, FileInputStream fis) throws IOException {
        // PDF files start with "%PDF"
        return new String(signature).equals("%PDF") || checkPDFMagicBytes(fis);
    }

    // Method to check additional bytes for PDF files
    private boolean checkPDFMagicBytes(FileInputStream fis) throws IOException {
        byte[] additionalBytes = new byte[2];
        fis.read(additionalBytes);

        // PDF files may have additional bytes after "%PDF"
        return additionalBytes[0] == (byte) 0x0D && additionalBytes[1] == (byte) 0x0A;
    }

    // Method to check if the file signature matches JPEG format
    private boolean isJPEG(byte[] signature) {
        return signature[0] == (byte) 0xFF &&
                signature[1] == (byte) 0xD8 &&
                signature[2] == (byte) 0xFF &&
                signature[3] == (byte) 0xE0;
    }

    // Method to check if the file signature matches PNG format
    private boolean isPNG(byte[] signature) {
        return signature[0] == (byte) 0x89 &&
                signature[1] == (byte) 0x50 &&
                signature[2] == (byte) 0x4E &&
                signature[3] == (byte) 0x47;
    }

}
