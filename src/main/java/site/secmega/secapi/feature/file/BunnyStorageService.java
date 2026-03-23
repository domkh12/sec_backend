package site.secmega.secapi.feature.file;

import java.io.IOException;
import java.io.InputStream;

public interface BunnyStorageService {
    String uploadFile(String fileName, InputStream inputStream, long fileSize) throws IOException;
    void deleteFile(String fileName) throws IOException;
}
