package co.istad.rotana.ecommerce.features.auth;

import co.istad.rotana.ecommerce.features.auth.dto.RegisterRequest;
import co.istad.rotana.ecommerce.features.auth.dto.RegisterResponse;
import co.istad.rotana.ecommerce.security.KeycloakProperties;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        //validate pw :
        if(!registerRequest.password().equals(registerRequest.confirmedPassword())
        )throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password do not match");

        //create keycloak user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEnabled(true);
        user.setEmailVerified(false);
        //custom attribute
        Map<String,List<String>> attributes = new HashMap<>();
        attributes.put("gender",List.of(registerRequest.gender()));
        attributes.put("biography",List.of(registerRequest.biography()));
        user.setAttributes(attributes);

        //set pw :
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.password());
        user.setCredentials(List.of(credential));

        UsersResource usersResource = keycloak.realm(keycloakProperties
                .getRealm()).users();


        //start saving via api to keycloak
        try(Response response = usersResource.create(user)){
            log.info("status code : " + response.getStatus());
            if (response.getStatus()==HttpStatus.CREATED.value()){
                UserRepresentation createdUser = usersResource
                        .search(user.getUsername()).getFirst();
                log.info("created user : "+ createdUser.getId());
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
        return null;
    }
}
