package co.istad.rotana.ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakAdminClientConfig {
    private final KeycloakProperties keycloakProps;
    @Bean
    public Keycloak configureAdminClient(){
        IO.println("keycloak is : "+keycloakProps);
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(keycloakProps.getServerUrl())
                .realm(keycloakProps.getRealm())
                .clientId(keycloakProps.getClientId())
                .clientSecret(keycloakProps.getClienSecret())
                .build();

    }
        
    }
