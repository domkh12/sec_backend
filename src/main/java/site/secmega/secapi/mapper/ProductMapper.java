package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Product;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product fromProductRequest(ProductRequest productRequest);
    @Mapping(target = "size", expression = "java(product.getSizes() != null ? product.getSizes().stream().map(s -> s.getSize()).collect(java.util.stream.Collectors.joining(\", \")) : null)")
    @Mapping(target = "color", expression = "java(product.getColors() != null ? product.getColors().stream().map(s -> s.getColor()).collect(java.util.stream.Collectors.joining(\", \")) : null)")
    @Mapping(target = "subCategory", expression = "java(product.getSubCategory() != null ? product.getSubCategory().getName() : null)")
    ProductResponse toProductResponse(Product product);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromProductRequest(ProductRequest productRequest, @MappingTarget Product product);
}
