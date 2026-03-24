package site.secmega.secapi.feature.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Service
@Slf4j
public class BunnyStorageServiceImpl implements BunnyStorageService {

    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    private S3Client getClient() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .region(Region.of("auto")) // required for R2
                .build();
    }

    @Override
    public String uploadFile(String fileName, InputStream inputStream, long fileSize) throws IOException {
        S3Client s3 = getClient();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType("application/octet-stream")
                    .build();

            s3.putObject(request, RequestBody.fromInputStream(inputStream, fileSize));

            // Return CDN/public URL
            return publicUrl + "/" + fileName;

        } catch (Exception e) {
            log.error("R2 upload failed", e);
            throw new IOException("Failed to upload file to R2", e);
        }
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        S3Client s3 = getClient();

        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3.deleteObject(request);

        } catch (Exception e) {
            log.error("R2 delete failed", e);
            throw new IOException("Failed to delete file from R2", e);
        }
    }
}