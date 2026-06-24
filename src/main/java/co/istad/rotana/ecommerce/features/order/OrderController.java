package co.istad.rotana.ecommerce.features.order;

import co.istad.rotana.ecommerce.features.order.dto.CreateOrderRequest;
import co.istad.rotana.ecommerce.features.order.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderResponse createNew(
            @Valid @RequestBody CreateOrderRequest createOrderRequest,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return orderService.createOrder(createOrderRequest);
    }






}
