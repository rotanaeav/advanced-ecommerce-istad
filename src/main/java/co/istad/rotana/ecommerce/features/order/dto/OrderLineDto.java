package co.istad.rotana.ecommerce.features.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderLineDto(
        @NotBlank(message = "Product code is required")
        String productCode,
        @Positive(message = "Quantity must be positive")
        Integer qty,
        @Min(0)
        float discount
) {
}
