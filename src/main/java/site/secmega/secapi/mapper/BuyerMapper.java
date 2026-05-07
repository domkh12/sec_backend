package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Buyer;
import site.secmega.secapi.feature.buyer.dto.BuyerLookupResponse;
import site.secmega.secapi.feature.buyer.dto.BuyerRequest;
import site.secmega.secapi.feature.buyer.dto.BuyerResponse;

@Mapper(componentModel = "spring")
public interface BuyerMapper {
    BuyerResponse toBuyerResponse(Buyer buyer);
    Buyer fromBuyerRequest(BuyerRequest buyerRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromBuyerRequest(BuyerRequest buyerRequest, @MappingTarget Buyer buyer);
    BuyerLookupResponse toBuyerLookupResponse(Buyer buyer);
}
