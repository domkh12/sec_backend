package site.secmega.secapi.feature.qr.dto;

public record QrDataRequest(
        String mo,
        String size,
        Integer qty
) {
}
