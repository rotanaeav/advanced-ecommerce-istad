package co.istad.rotana.ecommerce.features.auth.dto;

import lombok.Builder;

@Builder
public record RegisterResponse(
        String keycloakUserId,
        String username,
        String email,
        String firstName,
        String lastName,
        String gender,
        String biography
) {
}
