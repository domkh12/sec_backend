package site.secmega.secapi.feature.qr;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QRCodeController {
    private final QRCodeService qrCodeService;

    @GetMapping("/generateQRCode")
    ResponseEntity<byte []> generateQRCode(@RequestParam String data) throws IOException, WriterException {
        return qrCodeService.generateQRCode(data);
    }

    @PostMapping("/readQRCode")
    ResponseEntity<String> readQRCode(@RequestParam("file") MultipartFile file) throws IOException {
        return qrCodeService.readQRCode(file.getBytes());
    }


}
