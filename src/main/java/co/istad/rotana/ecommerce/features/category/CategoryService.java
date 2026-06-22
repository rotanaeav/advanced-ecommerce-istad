package co.istad.rotana.ecommerce.features.category;

import co.istad.rotana.ecommerce.features.category.dto.CategoryResponse;
import co.istad.rotana.ecommerce.features.category.dto.CreateCategoryRequest;
import co.istad.rotana.ecommerce.features.category.dto.UpdateCategoryRequest;
import org.springframework.data.domain.Page;

public interface CategoryService {

    CategoryResponse create(CreateCategoryRequest request);

    Page<CategoryResponse> findAll(int pageNumber, int pageSize);

    CategoryResponse findById(Integer id);

    CategoryResponse updateById(Integer id, UpdateCategoryRequest request);

    void deleteById(Integer id);

}