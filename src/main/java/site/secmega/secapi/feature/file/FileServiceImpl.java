package site.secmega.secapi.feature.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.file.dto.FileResponse;
import site.secmega.secapi.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class FileServiceImpl implements FileService{

    @Value("${file-server.base-uri}")
    private String baseUri;

    @Value("${file-server.server-path}")
    private String serverPath;

    @Override
    public FileResponse uploadFile(MultipartFile file) throws IOException {
        String newFileName = FileUtil.generateNewFileName(file.getOriginalFilename());
        String extension = FileUtil.extractExtension(file.getOriginalFilename());
        Path path = Path.of(serverPath, newFileName);
        Files.copy(file.getInputStream(), path);

        return FileResponse.builder()
                .name(newFileName)
                .size(file.getSize())
                .extension(extension)
                .uri(baseUri + newFileName)
                .build();
    }
}
