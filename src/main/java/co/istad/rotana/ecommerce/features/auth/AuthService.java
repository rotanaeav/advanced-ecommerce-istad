package co.istad.rotana.ecommerce.features.auth;

import co.istad.rotana.ecommerce.features.auth.dto.RegisterRequest;
import co.istad.rotana.ecommerce.features.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);
}
