package site.secmega.secapi.feature.qr;

import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface QRCodeService {
    ResponseEntity<byte[]> generateQRCode(String data) throws WriterException, IOException;

    ResponseEntity<String> readQRCode(byte[] file) throws IOException;
}
