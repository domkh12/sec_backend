package site.secmega.secapi.feature.subCategory;

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
import site.secmega.secapi.feature.category.CategoryRepository;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryFilterRequest;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryRequest;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryResponse;
import site.secmega.secapi.mapper.SubCategoryMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService{

    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper subCategoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public void deleteSubCategory(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sub category not found!")
        );
        subCategory.setDeletedAt(LocalDateTime.now());
        subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategoryResponse updateSubCategory(Long id, SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sub category not found!")
        );
        subCategoryMapper.updateFromSubCategoryRequest(subCategoryRequest, subCategory);
        if (subCategoryRequest.categoryId() != null){
            Category category = categoryRepository.findById(subCategoryRequest.categoryId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!")
            );
            subCategory.setCategory(category);
        }
        subCategory.setUpdatedAt(LocalDateTime.now());
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryMapper.toSubCategoryResponse(updatedSubCategory);
    }

    @Override
    public SubCategoryResponse createSubCategory(SubCategoryRequest subCategoryRequest) {
        if (subCategoryRepository.existsByDeletedAtNull()){
            throw new IllegalArgumentException("Sub category already exist");
        }

        SubCategory subCategory = subCategoryMapper.fromSubCategoryRequest(subCategoryRequest);
        if (subCategoryRequest.categoryId() != null){
            Category category = categoryRepository.findById(subCategoryRequest.categoryId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!")
            );
            subCategory.setCategory(category);
        }
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryMapper.toSubCategoryResponse(savedSubCategory);
    }

    @Override
    public Page<SubCategoryResponse> findAll(SubCategoryFilterRequest subCategoryFilterRequest) {
        if (subCategoryFilterRequest.pageNo() <= 0 || subCategoryFilterRequest.pageSize() <= 0 ){
            throw new IllegalArgumentException("Page no and Page size must be greater than 0");
        }

        Specification<SubCategory> spec = Specification.where((root, query, cb) -> cb.conjunction());

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(subCategoryFilterRequest.pageNo() - 1, subCategoryFilterRequest.pageSize(), sort);
        Page<SubCategory> subCategories = subCategoryRepository.findAll(spec, pageRequest);

        return subCategories.map(subCategoryMapper::toSubCategoryResponse);
    }
}
