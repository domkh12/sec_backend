package site.secmega.secapi.feature.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.ProductStatus;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.category.CategoryRepository;
import site.secmega.secapi.feature.color.ColorRepository;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.subCategory.SubCategoryRepository;
import site.secmega.secapi.mapper.ProductMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final SubCategoryRepository subCategoryRepository;

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

        productMapper.updateFromProductRequest(productRequest, product);
        product.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        if (productRepository.existsByStyleNoIgnoreCaseAndDeletedAtNull(productRequest.styleNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Style number already exist");
        }

        Product product = productMapper.fromProductRequest(productRequest);
        if (productRequest.sizeId() != null){
            List<Size> sizes = sizeRepository.findByIdIn(productRequest.sizeId());
            product.setSizes(sizes);
        }

        if (productRequest.colorId() != null){
            List<Color> colors = colorRepository.findByIdIn(productRequest.colorId());
            product.setColors(colors);
        }

        if (productRequest.subCategoryId() != null){
            SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sub Category no found!")
            );
            product.setSubCategory(subCategory);
        }

        product.setStatus(ProductStatus.Draft);

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
