package co.istad.rotana.ecommerce.features.order;

import co.istad.rotana.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.rotana.ecommerce.features.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest createOrderRequest ,Jwt jwt);

}
