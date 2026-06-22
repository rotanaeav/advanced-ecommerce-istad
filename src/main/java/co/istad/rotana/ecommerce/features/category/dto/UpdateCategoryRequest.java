package co.istad.rotana.ecommerce.features.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(max = 50, message = "Over 50 characters")
        String name,

        String description,

        String icon

) {
}