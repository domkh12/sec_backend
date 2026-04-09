package site.secmega.secapi.feature.material.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StockOutResponse(
        Long id,
        String materialName,
        Double qtyOutput,
        Double qtyBalance,
        String requester,
        @JsonFormat(pattern = "dd/MMM/yyy hh:mma")
        LocalDateTime dateOutput
) {
}
