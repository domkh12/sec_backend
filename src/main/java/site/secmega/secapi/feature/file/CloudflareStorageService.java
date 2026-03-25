package site.secmega.secapi.feature.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface CloudflareStorageService {
    String uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String fileName) throws IOException;
}
