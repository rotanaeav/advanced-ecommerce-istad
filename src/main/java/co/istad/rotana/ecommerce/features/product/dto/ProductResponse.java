package co.istad.rotana.ecommerce.features.product.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        String code,
        String name,
        BigDecimal price,
        Integer qty,
        String description,
        Boolean isAvailable,
        String categoryName
) {
}
