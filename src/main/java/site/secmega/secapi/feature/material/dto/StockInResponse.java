package site.secmega.secapi.feature.material.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record StockInResponse(
        Long id,
        String materialName,
        Double qtyInput,
        Double qtyBalance,
        String user,
        String unit,
        @JsonFormat(pattern = "dd/MMM/yyy hh:mma")
        LocalDateTime dateInput
) {
}
