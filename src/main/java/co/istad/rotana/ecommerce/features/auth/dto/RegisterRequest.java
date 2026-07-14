package co.istad.rotana.ecommerce.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min=3,max = 255)
        String username,
        @NotBlank(message = "Password is required")
        @Size(max = 255)
        String password,
        @NotBlank(message = "Confirmed Password is required")
        @Size(max = 255)
        String confirmedPassword,
        @NotBlank(message = "Email is required")
        @Size(max = 255)
        @Email
        String email,
        @NotBlank(message = "First name is required")
        @Size(max = 255)
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(max = 255)
        String lastName,
        @Size(max = 6)
        String gender,
        @Size(max = 255)
        String biography
) {
}
