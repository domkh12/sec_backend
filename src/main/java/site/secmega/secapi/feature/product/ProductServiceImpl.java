package site.secmega.secapi.feature.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.ProductStatus;
import site.secmega.secapi.domain.Category;
import site.secmega.secapi.domain.Product;
import site.secmega.secapi.feature.category.CategoryRepository;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;
import site.secmega.secapi.mapper.ProductMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
        if (productRepository.existsByCodeAndDeletedAtNullAndIdNot(productRequest.code(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product code already exist");
        }
        if (productRequest.cateId() != null){
            Category category = categoryRepository.findById(productRequest.cateId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!")
            );
            product.setCategory(category);
        }
        productMapper.updateFromProductRequest(productRequest, product);
        product.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRepository.existsByCode(productRequest.code())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product code already exist");
        }

        Category category = categoryRepository.findById(productRequest.cateId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!")
        );

        Product product = productMapper.fromProductRequest(productRequest);
        product.setStatus(ProductStatus.Draft);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public Page<ProductResponse> getProducts(Integer pageNo, Integer pageSize) {
        if (pageNo <= 0 || pageSize <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Product> products = productRepository.findAll(pageRequest);

        return products.map(productMapper::toProductResponse);
    }
}
