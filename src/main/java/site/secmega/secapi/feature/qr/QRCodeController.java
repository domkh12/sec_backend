package site.secmega.secapi.feature.qr;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.qr.dto.QrDataRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/generateQRCode")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<byte []> generateQRCode(@RequestBody QrDataRequest data) throws IOException, WriterException {
        return qrCodeService.generateQRCode(data);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/readQRCode")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<String> readQRCode(@RequestParam("file") MultipartFile file) throws IOException {
        return qrCodeService.readQRCode(file.getBytes());
    }


}
