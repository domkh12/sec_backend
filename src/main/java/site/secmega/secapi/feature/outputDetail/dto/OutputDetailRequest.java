package site.secmega.secapi.feature.outputDetail.dto;

public record OutputDetailRequest(
        Long timeId,
        Long sizeId,
        Long lineId,
        String mo,
        Integer goodQty

) {
}
