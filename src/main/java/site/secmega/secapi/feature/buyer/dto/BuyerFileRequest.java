package site.secmega.secapi.feature.buyer.dto;

import java.util.List;

public record BuyerFileRequest(
        Long id,
        List<String> files
) {
}
