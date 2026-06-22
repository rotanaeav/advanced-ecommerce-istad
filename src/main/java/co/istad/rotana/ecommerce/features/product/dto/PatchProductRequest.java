package co.istad.rotana.ecommerce.features.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PatchProductRequest(
        @Size(min = 1, max = 100)
        String name,

        String description,

        @Positive
        BigDecimal price,

        @Positive
        Integer qty,

        @Positive
        Integer categoryId,

        Boolean isAvailable
) {
}
