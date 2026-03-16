package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Product;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product fromProductRequest(ProductRequest productRequest);
    @Mapping(target = "category", expression = "java(product.getCategory() != null ? product.getCategory().getName() : null)")
    @Mapping(target = "cateId", expression = "java(product.getCategory().getId() != null ? product.getCategory().getId() : null)")
    ProductResponse toProductResponse(Product product);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromProductRequest(ProductRequest productRequest, @MappingTarget Product product);
}
