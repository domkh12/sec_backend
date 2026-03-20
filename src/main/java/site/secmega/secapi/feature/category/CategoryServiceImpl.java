package site.secmega.secapi.feature.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Category;
import site.secmega.secapi.domain.SubCategory;
import site.secmega.secapi.feature.category.dto.CategoryFilterRequest;
import site.secmega.secapi.feature.category.dto.CategoryRequest;
import site.secmega.secapi.feature.category.dto.CategoryResponse;
import site.secmega.secapi.mapper.CategoryMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );
        categoryMapper.updateFromCategoryRequest(categoryRequest, category);
        category.setUpdatedAt(LocalDateTime.now());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByNameIgnoreCaseAndDeletedAtNull(categoryRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category name already exist");
        }
        Category category = categoryMapper.fromCategoryRequest(categoryRequest);
        category.setCreatedAt(LocalDateTime.now());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public Page<CategoryResponse> findAll(CategoryFilterRequest categoryFilterRequest) {

        if (categoryFilterRequest.pageNo() < 1 || categoryFilterRequest.pageSize() < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no or Page size must greater than 0");
        }

        Specification<Category> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (categoryFilterRequest.search() != null){
            String searchTerm = "%" + categoryFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")), searchTerm)
                    )
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(categoryFilterRequest.pageNo() - 1, categoryFilterRequest.pageSize(), sort);
        Page<Category> categories = categoryRepository.findAll(spec ,pageRequest);
        return categories.map(categoryMapper::toCategoryResponse);
    }
}
