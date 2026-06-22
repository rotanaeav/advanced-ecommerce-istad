package co.istad.rotana.ecommerce.features.order;

import co.istad.rotana.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.rotana.ecommerce.features.order.dto.OrderLineDto;
import co.istad.rotana.ecommerce.features.order.dto.OrderResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order mapCreateOrderRequestToOrder(CreateOrderRequest createOrderRequest);
    OrderResponse mapOrderToOrderResponse(Order order);

    default List<OrderLineDto> mapOrderLineToOrderLineDto(List<OrderLine> orderLines) {
        return orderLines.stream()
                .map(orderLine -> OrderLineDto.builder()
                        .productCode(orderLine.getProduct().getCode())
                        .qty(orderLine.getQty())
                        .discount(orderLine.getDiscount())
                        .build())
                .collect(Collectors.toList());
    }
}

