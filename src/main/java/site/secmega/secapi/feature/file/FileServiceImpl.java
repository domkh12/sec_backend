package site.secmega.secapi.feature.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.feature.file.dto.FileResponse;
import site.secmega.secapi.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public List<FileResponse> findAll() {

        Path path = Path.of(serverPath);
        File[] files = path.toFile().listFiles();
        List<FileResponse> fileResponses = new ArrayList<>();
        for (File file : files) {
            fileResponses.add(
                    FileResponse.builder()
                            .name(file.getName())
                            .size(file.length())
                            .extension(FileUtil.extractExtension(file.getName()))
                            .uri(baseUri + file.getName())
                            .build()
            );
        }
        return fileResponses;
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        Path path = Paths.get(serverPath);
        path = path.resolve(fileName);
        if (!Files.exists(path)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
        Files.deleteIfExists(path);
    }
}
