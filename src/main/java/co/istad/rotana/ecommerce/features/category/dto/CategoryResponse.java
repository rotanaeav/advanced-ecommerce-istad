package co.istad.rotana.ecommerce.features.category.dto;

public record CategoryResponse(
        Integer id,
        String name,
        String description,
        String icon
) {
}