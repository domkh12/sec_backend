package site.secmega.secapi.feature.file;

import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.file.dto.FileResponse;

import java.io.IOException;

public interface FileService {
    FileResponse uploadFile(MultipartFile file) throws IOException;
}
