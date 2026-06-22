package co.istad.rotana.ecommerce.features.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        String remark,
        @NotEmpty(message = "Order must have least 1 item ")
        List<@NotNull(message = "Must not null")OrderLineDto> orderLines
) {
}
