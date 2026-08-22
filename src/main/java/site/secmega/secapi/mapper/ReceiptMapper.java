package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Receipt;
import site.secmega.secapi.feature.receipt.dto.ReceiptRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptResponse;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {
    ReceiptResponse toReceiptResponse(Receipt receipt);
    Receipt fromReceiptRequest(ReceiptRequest receiptRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromReceiptRequest(ReceiptRequest receiptRequest,@MappingTarget Receipt receipt);
}
