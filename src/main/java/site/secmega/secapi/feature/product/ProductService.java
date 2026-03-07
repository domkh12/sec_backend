package site.secmega.secapi.feature.product;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;

public interface ProductService {

    Page<ProductResponse> getProducts(Integer pageNo, Integer pageSize);

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);
}
