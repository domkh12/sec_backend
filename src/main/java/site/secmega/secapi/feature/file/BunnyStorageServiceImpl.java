package site.secmega.secapi.feature.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class BunnyStorageServiceImpl implements BunnyStorageService {

    @Value("${bunny.storage-zone}")
    private String storageZone;

    @Value("${bunny.api-key}")
    private String apiKey;

    @Value("${bunny.cdn-url}")
    private String cdnUrl;

    @Value("${bunny.region:}")
    private String region;

    private String getStorageHost() {
        if (region == null || region.isBlank()) {
            return "storage.bunnycdn.com";
        }
        return region + ".storage.bunnycdn.com";
    }

    @Override
    public String uploadFile(String fileName, InputStream inputStream, long fileSize) throws IOException {
        String uploadUrl = String.format("https://%s/%s/%s",
                getStorageHost(), storageZone, fileName);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("AccessKey", apiKey)
                .header("Content-Type", "application/octet-stream")
                .PUT(HttpRequest.BodyPublishers.ofInputStream(() -> inputStream))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                log.error("Bunny.net upload failed: {} - {}", response.statusCode(), response.body());
                throw new IOException("Failed to upload file to Bunny.net: " + response.statusCode());
            }

            // Return the public CDN URL
            return cdnUrl + "/" + fileName;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Upload interrupted", e);
        }
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        String deleteUrl = String.format("https://%s/%s/%s",
                getStorageHost(), storageZone, fileName);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deleteUrl))
                .header("AccessKey", apiKey)
                .DELETE()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Bunny.net delete failed: {} - {}", response.statusCode(), response.body());
                throw new IOException("Failed to delete file from Bunny.net: " + response.statusCode());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Delete interrupted", e);
        }
    }
}