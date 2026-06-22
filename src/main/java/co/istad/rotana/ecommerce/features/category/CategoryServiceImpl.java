package co.istad.rotana.ecommerce.features.category;

import co.istad.rotana.ecommerce.features.category.dto.CategoryResponse;
import co.istad.rotana.ecommerce.features.category.dto.CreateCategoryRequest;
import co.istad.rotana.ecommerce.features.category.dto.UpdateCategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {

        if (categoryRepository.existsByName(request.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category name already exists"
            );
        }

        Category category = categoryMapper.createCategoryRequestToCategory(request);

        category = categoryRepository.save(category);

        return categoryMapper.categoryToCreateCategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> findAll(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return categoryRepository.findAll(pageable)
                .map(categoryMapper::categoryToCreateCategoryResponse);
    }

    @Override
    public CategoryResponse findById(Integer id) {

        return categoryRepository.findById(id)
                .map(categoryMapper::categoryToCreateCategoryResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category ID not found"
                ));
    }

    @Override
    public CategoryResponse updateById(Integer id, UpdateCategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category ID not found"
                ));

        if (categoryRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category name already exists"
            );
        }

        categoryMapper.updateCategoryRequestToCategory(request, category);

        category = categoryRepository.save(category);

        return categoryMapper.categoryToCreateCategoryResponse(category);
    }

    @Override
    public void deleteById(Integer id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category ID not found"
                ));

        categoryRepository.delete(category);
    }

}