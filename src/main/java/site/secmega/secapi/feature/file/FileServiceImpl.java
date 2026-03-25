package site.secmega.secapi.feature.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.FileMetadata;
import site.secmega.secapi.feature.file.dto.FileResponse;
import site.secmega.secapi.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final FileRepository fileRepository;
    private final CloudflareStorageService cloudflareStorageService;

    @Override
    public List<FileResponse> uploadMultipleFiles(List<MultipartFile> files) {
        log.info("Received {} files for upload", files.size());
        List<FileResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                FileResponse response = uploadFile(file);
                responses.add(response);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file");
            }
        }
        return responses;
    }

    @Override
    public FileResponse uploadFile(MultipartFile file) throws IOException {
        String newFileName = FileUtil.generateNewFileName(file.getOriginalFilename());
        String extension = FileUtil.extractExtension(file.getOriginalFilename());

        // Upload to Bunny CDN
        String cdnUrl = cloudflareStorageService.uploadFile(file);

        return FileResponse.builder()
                .name(newFileName)
                .size(file.getSize())
                .extension(extension)
                .uri(cdnUrl)
                .build();
    }

    @Override
    public List<FileResponse> findAll() {

        return fileRepository.findAll().stream()
                .map(f -> FileResponse.builder()
                        .name(f.getStoredName())
                        .uri(f.getUri()) // store CDN url in DB
                        .build())
                .toList();
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        cloudflareStorageService.deleteFile(fileName);
    }

    @Scheduled(fixedRate = 4 * 60 * 60 * 1000) // every 4 hours
//    @Scheduled(fixedRate = 30000)
    public void cleanupOldFiles() {

        List<FileMetadata> oldFiles = fileRepository.findByCurrentFalseAndStoredNameNot();
        List<FileMetadata> oldFiles2 = fileRepository.findByCurrentFalseAndStoredName();
        fileRepository.deleteAll(oldFiles2);
        oldFiles.forEach(file -> {
            try {
                deleteFile(file.getStoredName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileRepository.delete(file);
            log.info("Cleaned up old file: {}", file.getStoredName());
        });
    }
}
