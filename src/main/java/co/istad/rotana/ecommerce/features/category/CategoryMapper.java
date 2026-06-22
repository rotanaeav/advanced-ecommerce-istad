package co.istad.rotana.ecommerce.features.category;

import co.istad.rotana.ecommerce.features.category.dto.CategoryResponse;
import co.istad.rotana.ecommerce.features.category.dto.CreateCategoryRequest;
import co.istad.rotana.ecommerce.features.category.dto.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category createCategoryRequestToCategory(CreateCategoryRequest createCategoryRequest);

    CategoryResponse categoryToCreateCategoryResponse(Category category);

    void updateCategoryRequestToCategory(UpdateCategoryRequest updateCategoryRequest,
                                         @MappingTarget Category category);

}