package co.istad.rotana.ecommerce.features.auth;

import co.istad.rotana.ecommerce.features.auth.dto.RegisterRequest;
import co.istad.rotana.ecommerce.features.auth.dto.RegisterResponse;
import co.istad.rotana.ecommerce.features.userprofile.UserProfile;
import co.istad.rotana.ecommerce.features.userprofile.UserProfileRepository;
import co.istad.rotana.ecommerce.security.KeycloakProperties;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserProfileRepository userProfileRepository;
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProps;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        // Validate password
        if (!registerRequest.password().equals(
                registerRequest.confirmedPassword()
        )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Passwords do not match");
        }

        // Create keycloak UserRepresentation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEnabled(true);
        user.setEmailVerified(false);

        // prepare customized attributes (gender, biography)
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("gender", List.of(registerRequest.gender()));
        attributes.put("biography", List.of(registerRequest.biography()));
        attributes.put("phoneNumber", List.of(registerRequest.phoneNumber()));
        user.setAttributes(attributes);

        // set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.password());
        user.setCredentials(List.of(credential));

        UsersResource usersResource = keycloak
                .realm(keycloakProps.getRealm())
                .users();

        // Start saving user into keycloak via API
        try (Response response = usersResource.create(user)) {
            log.info("Response status code: {}", response.getStatus());
            if (response.getStatus() != HttpStatus.CREATED.value()) {
                String keycloakMessage = response.hasEntity()
                        ? response.readEntity(String.class)
                        : "No response body";
                HttpStatus status = HttpStatus.resolve(response.getStatus());

                if (status == HttpStatus.CONFLICT) {
                    throw new ResponseStatusException(status,
                            "Username or email already exists");
                }
                if (status == HttpStatus.BAD_REQUEST) {
                    throw new ResponseStatusException(status,
                            "Identity provider rejected registration data");
                }
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Failed to create user in identity provider: " + keycloakMessage);
            }

            Optional<UserRepresentation> createdUserOptional = usersResource
                    .search(user.getUsername())
                    .stream()
                    .findFirst();

            if (createdUserOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "User was created but could not be retrieved from identity provider");
            }

            UserRepresentation createdUser = createdUserOptional.get();
            log.info("Created user {}", createdUser.getId());

            // Start saving user into database
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(createdUser.getId());
            userProfileRepository.save(userProfile);

            return RegisterResponse.builder()
                    .keycloakUserId(createdUser.getId())
                    .username(createdUser.getUsername())
                    .email(createdUser.getEmail())
                    .firstName(createdUser.getFirstName())
                    .lastName(createdUser.getLastName())
                    .gender(createdUser.firstAttribute("gender"))
                    .biography(createdUser.firstAttribute("biography"))
                    .build();
        }
    }

}