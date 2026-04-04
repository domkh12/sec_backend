package site.secmega.secapi.feature.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeServiceImpl implements QRCodeService{
    @Override
    public ResponseEntity<byte[]> generateQRCode(String data) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().fillRect(0, 0, 200, 200);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setColor(Color.black);

        for (int x = 0; x < 200; x++){
            for (int y = 0; y < 200; y++){
                if (bitMatrix.get(x, y)){
                    graphics2D.fillRect(x, y, 1, 1);
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    @Override
    public ResponseEntity<String> readQRCode(byte[] file) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(file);
        BufferedImage bufferedImage = ImageIO.read(bais);

        if (bufferedImage == null) {
            return ResponseEntity.badRequest().body("Invalid or unsupported image format");
        }

        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        HybridBinarizer binarizer = new HybridBinarizer(source);
        com.google.zxing.BinaryBitmap binaryBitmap = new com.google.zxing.BinaryBitmap(binarizer);
        MultiFormatReader reader = new MultiFormatReader();

        try{
            Result result = reader.decode(binaryBitmap);
            return ResponseEntity.ok(result.getText());
        }catch (Exception e){
            return ResponseEntity.ok("Not a QR Code");
        }
    }
}
