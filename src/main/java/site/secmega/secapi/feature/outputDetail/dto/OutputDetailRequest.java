package site.secmega.secapi.feature.outputDetail.dto;

import jakarta.validation.constraints.NotNull;

public record OutputDetailRequest(
        @NotNull(message = "Time cannot be null")
        Long timeId,
        @NotNull(message = "Size cannot be null")
        Long sizeId,
        @NotNull(message = "Line cannot be null")
        Long lineId,
        String mo,
        Integer goodQty

) {
}
