package site.secmega.secapi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.secmega.secapi.domain.FileMetadata;
import site.secmega.secapi.feature.file.FileRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileUtil {

    private final FileRepository fileRepository;

    public static String generateNewFileName(String fileName){
        String newFile = UUID.randomUUID().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        return String.format("%s%s", newFile, extension);
    }

    public static String extractExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public void updateFile(Long userId, String ownerType, String uri) {

        FileMetadata newMeta = FileMetadata.builder()
                .storedName(uri.substring(uri.lastIndexOf("/") + 1))
                .ownerType(ownerType)
                .uri(uri)
                .ownerId(userId)
                .current(true)
                .uploadedAt(LocalDateTime.now())
                .build();

        fileRepository.save(newMeta);
    }

    public void deleteFile(Long userId, String ownerType, String uri) {
        fileRepository.findByOwnerIdAndOwnerTypeAndCurrentTrueAndUri(userId, ownerType, uri)
                .ifPresent(old -> {
                    old.setCurrent(false);
                    old.setReplacedAt(LocalDateTime.now()); // 👈 mark when it became old
                    fileRepository.save(old);
                });
    }
}
