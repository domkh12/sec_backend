package site.secmega.secapi.feature.outputDetail.dto;

import jakarta.validation.constraints.NotNull;

public record OutputDetailRequest(
        @NotNull(message = "Time cannot be null")
        Long timeId,
        @NotNull(message = "Size cannot be null")
        Long sizeId,
        @NotNull(message = "From Line cannot be null")
        Long fromLineId,
        @NotNull(message = "To Line cannot be null")
        Long toLineId,
        String mo,
        Integer goodQty

) {
}
