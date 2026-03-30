package site.secmega.secapi.feature.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.util.FileUtil;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudflareStorageServiceImpl implements CloudflareStorageService {

   private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String original = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filename is missing"))
                .toLowerCase();
        String newFileName = FileUtil.generateNewFileName(original);
        String contentType = Optional.ofNullable(file.getContentType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content-Type is unknown"));

        String ext = FileUtil.extractExtension(original);
        String folder = switch (ext) {
            case "jpg", "jpeg", "png", "gif", "svg" -> "images";
            case "mp4", "mov"                -> "videos";
            case "pdf", "doc", "docx", "txt", "xlsx", "xlsm", "xls", "csv", "json", "zip", "rar" -> "documents";
            default -> throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported file type: " + ext);
        };

        String key = String.format("%s/%s", folder, newFileName);
        log.info("Uploading file to R2: {}", key);
        log.info("File name: {}", newFileName);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            log.error("R2 upload failed for file: {}", original, e);
            throw new IOException("Failed to upload file to R2", e);
        }

        return publicUrl + "/" + key;
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(request);
        } catch (Exception e) {
            log.error("R2 delete failed for file: {}", fileName, e);
            throw new IOException("Failed to delete file from R2", e);
        }
    }
}