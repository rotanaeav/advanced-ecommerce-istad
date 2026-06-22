package co.istad.rotana.ecommerce.features.order;

import co.istad.rotana.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.rotana.ecommerce.features.order.dto.OrderResponse;
import co.istad.rotana.ecommerce.features.product.Product;
import co.istad.rotana.ecommerce.features.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest, Jwt jwt) {

        List<OrderLine> validOrderLines = new ArrayList<>();
        final Order order = orderMapper.mapCreateOrderRequestToOrder(createOrderRequest);

        // Validate productcode
        boolean isValid = createOrderRequest.orderLines().stream()
                .allMatch(orderLineDto -> {
                    boolean isExisting = productRepository.existsById(orderLineDto.productCode());
                    if (isExisting) {
                        Product validProduct = productRepository.findById(orderLineDto.productCode())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product code has not been found"));
                        OrderLine orderLine = new OrderLine();
                        orderLine.setProduct(validProduct);
                        orderLine.setQty(orderLineDto.qty());
                        orderLine.setDiscount(orderLineDto.discount());
                        orderLine.setOrder(order);
                        validOrderLines.add(orderLine);
                    }
                    return isExisting;
                });

        if (!isValid)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Product code is invalid");

        // System data generation
        order.setOrderedAt(Instant.now());
        order.setIsDeleted(false);
        order.setOrderedBy(jwt.getSubject());
        order.setOrderLines(validOrderLines);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapOrderToOrderResponse(savedOrder);
    }

}