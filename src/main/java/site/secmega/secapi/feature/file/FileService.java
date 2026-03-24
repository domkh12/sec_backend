package site.secmega.secapi.feature.file;

import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.file.dto.FileResponse;

import java.io.IOException;
import java.util.List;

public interface FileService {
    FileResponse uploadFile(MultipartFile file) throws IOException;

    List<FileResponse> findAll();

    void deleteFile(String fileName) throws IOException;

    List<FileResponse> uploadMultipleFiles(List<MultipartFile> files);
}
