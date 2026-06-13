package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.MaterialDetail;
import site.secmega.secapi.feature.material.dto.UpdateStockInQtyRequest;

@Mapper(componentModel = "spring")
public interface MaterialDetailMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStockIn(UpdateStockInQtyRequest updateStockInQtyRequest, @MappingTarget MaterialDetail materialDetail);
}
