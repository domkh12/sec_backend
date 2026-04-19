package site.secmega.secapi.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;

import static java.lang.String.format;

public class Util {
    public static HttpHeaders getHttpHeaders(String code, File invoicePdf, String fileType, MediaType mediaType) {
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(mediaType);
        respHeaders.setContentLength(invoicePdf.length());
        respHeaders.setContentDispositionFormData("attachment", format("%s." + fileType, code));
        return respHeaders;
    }
}
