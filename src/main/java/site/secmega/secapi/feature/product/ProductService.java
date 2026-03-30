package site.secmega.secapi.feature.product;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.product.dto.ProductFilterRequest;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;
import site.secmega.secapi.feature.product.dto.ProductStateResponse;

public interface ProductService {

    Page<ProductResponse> getProducts(ProductFilterRequest productFilterRequest);

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);

    ProductStateResponse getProductState();
}
