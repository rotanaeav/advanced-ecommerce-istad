package co.istad.rotana.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) {
        // TODO: define your security configuration
        // 1. Security Mechanism -> OAuth 2.0 with keycloak
        http.oauth2ResourceServer(oauth2->oauth2
                .jwt(Customizer.withDefaults()));

        // 2. Session Stateless
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 3. Endpoints security
        http.authorizeHttpRequests(endpoint -> endpoint
//                .requestMatchers(HttpMethod.POST, "/api/v1/products/**")
//                .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyRole("BUSINESS", "ADMIN")
//                .requestMatchers(HttpMethod.PATCH, "/api/v1/products/**").hasAnyRole("BUSINESS", "ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/files/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/files/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/v1/files/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/file/**").permitAll()
                .anyRequest().authenticated()
        );

        // 4. Disable CSRF token
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
            Collection<String> roles = realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        };

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}

