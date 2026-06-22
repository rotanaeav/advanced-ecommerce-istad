package co.istad.rotana.ecommerce.features.order.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        Instant orderedAt,
        String orderedBy,
        Boolean isDeleted,
        String remark,
        List<OrderLineDto> orderLines
) {
}
