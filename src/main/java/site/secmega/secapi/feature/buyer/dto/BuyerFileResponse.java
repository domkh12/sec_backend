package site.secmega.secapi.feature.buyer.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BuyerFileResponse(
        List<String> files
) {
}
