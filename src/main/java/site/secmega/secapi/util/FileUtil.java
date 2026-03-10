package site.secmega.secapi.util;

import java.util.UUID;

public class FileUtil {
    public static String generateNewFileName(String fileName){
        String newFile = UUID.randomUUID().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        return String.format("%s%s", newFile, extension);
    }

    public static String extractExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
