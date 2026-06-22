package co.istad.rotana.ecommerce.features.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 100)
        String name,

        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        @Positive
        Integer qty
) {
}
