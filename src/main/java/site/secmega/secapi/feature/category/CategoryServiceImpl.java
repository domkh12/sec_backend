package site.secmega.secapi.feature.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Category;
import site.secmega.secapi.feature.category.dto.CategoryRequest;
import site.secmega.secapi.feature.category.dto.CategoryResponse;
import site.secmega.secapi.mapper.CategoryMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.fromCategoryRequest(categoryRequest);
        category.setCreatedAt(LocalDateTime.now());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public Page<CategoryResponse> findAll(Integer pageNo, Integer pageSize) {

        if (pageNo < 1 || pageSize < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no or Page size must greater than 0");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(categoryMapper::toCategoryResponse);
    }
}
