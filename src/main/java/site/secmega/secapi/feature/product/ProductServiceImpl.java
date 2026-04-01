package site.secmega.secapi.feature.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.ProductStatus;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.category.CategoryRepository;
import site.secmega.secapi.feature.color.ColorRepository;
import site.secmega.secapi.feature.product.dto.ProductFilterRequest;
import site.secmega.secapi.feature.product.dto.ProductRequest;
import site.secmega.secapi.feature.product.dto.ProductResponse;
import site.secmega.secapi.feature.product.dto.ProductStateResponse;
import site.secmega.secapi.feature.productColor.ProductColorRepository;
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
    private final ProductColorRepository productColorRepository;

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public ProductStateResponse getProductState() {
        long totalProduct = productRepository.count();
        long totalActive = productRepository.countByStatus(ProductStatus.Active);
        long totalDraft = productRepository.countByStatus(ProductStatus.Draft);
        return ProductStateResponse.builder()
                .totalStyleNo(totalProduct)
                .totalActive(totalActive)
                .totalDraft(totalDraft)
                .build();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );

        productMapper.updateFromProductRequest(productRequest, product);
        if (productRequest.sizeId() != null){
            List<Size> sizes = sizeRepository.findByIdIn(productRequest.sizeId());
            product.setSizes(sizes);
        }

        if (productRequest.colorId() != null){
            List<ProductColor> productColors = productColorRepository.findByProductIdAndColorIds(id, productRequest.colorId());
            if (productColors.isEmpty()){
                productRequest.colorId().forEach(cid -> {
                    Color color = colorRepository.findById(cid).orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found!")
                    );
                    ProductColor newProductColor = new ProductColor();
                    newProductColor.setColor(color);
                    newProductColor.setProduct(product);
                    productColorRepository.save(newProductColor);
                });
            }else{
                productColors.forEach(pc -> pc.setColor(colorRepository.findById(pc.getColor().getId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found!")
                )));
                productColorRepository.saveAll(productColors);
            }
        }

        if (productRequest.subCategoryId() != null){
            SubCategory subCategory = subCategoryRepository.findById(productRequest.subCategoryId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sub Category no found!")
            );
            product.setSubCategory(subCategory);
        }
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
//            product.setColors(colors);
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
    public Page<ProductResponse> getProducts(ProductFilterRequest productFilterRequest) {
        if (productFilterRequest.pageNo() <= 0 || productFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Product> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (productFilterRequest.search() != null){
            String searchTerm = "%" + productFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("styleNo")), searchTerm)
                    )
            );
        }

        if (productFilterRequest.status() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), productFilterRequest.status())
            );
        }

        if (productFilterRequest.sizeId() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("sizes").get("id"), productFilterRequest.sizeId())
            );
        }

        if (productFilterRequest.colorId() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("productColors").get("color").get("id"), productFilterRequest.colorId())
            );
        }

        if (productFilterRequest.subCategoryId() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("subCategory").get("id"), productFilterRequest.subCategoryId())
            );
        }

//        Product product = new Product();
//        product.getProductColors().forEach(pc -> log.info(String.valueOf(pc.getColor())));

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(productFilterRequest.pageNo() - 1, productFilterRequest.pageSize(), sort);
        Page<Product> products = productRepository.findAll(spec, pageRequest);
        return products.map(productMapper::toProductResponse);
    }
}
