package site.secmega.secapi.feature.qr;

import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;
import site.secmega.secapi.feature.qr.dto.QrDataRequest;

import java.io.IOException;

public interface QRCodeService {
    ResponseEntity<byte[]> generateQRCode(QrDataRequest data) throws WriterException, IOException;

    ResponseEntity<String> readQRCode(byte[] file) throws IOException;
}
