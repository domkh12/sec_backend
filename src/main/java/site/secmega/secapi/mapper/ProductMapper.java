package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Product;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product fromProductRequest(ProductRequest productRequest);
    @Mapping(target = "size", expression = "java(product.getSizes() != null ? product.getSizes().stream().map(s -> s.getSize()).collect(java.util.stream.Collectors.joining(\", \")) : null)")
//    @Mapping(target = "color", expression = "java(product.getColors() != null ? product.getColors().stream().map(s -> s.getColor()).collect(java.util.stream.Collectors.joining(\", \")) : null)")
    @Mapping(target = "subCategory", expression = "java(product.getSubCategory() != null ? product.getSubCategory().getName() : null)")
//    @Mapping(target = "colorId", expression = "java(product.getColors() != null ? product.getColors().stream().map(s -> s.getId()).collect(java.util.stream.Collectors.toList()) : null)")
    @Mapping(target = "sizeId", expression = "java(product.getSizes() != null ? product.getSizes().stream().map(s -> s.getId()).collect(java.util.stream.Collectors.toList()) : null)")
    @Mapping(target = "categoryId" , expression = "java(product.getSubCategory() != null ? product.getSubCategory().getCategory().getId() : null)")
    @Mapping(target = "subCategoryId", expression = "java(product.getSubCategory() != null ? product.getSubCategory().getId() : null)")
    ProductResponse toProductResponse(Product product);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromProductRequest(ProductRequest productRequest, @MappingTarget Product product);
}
