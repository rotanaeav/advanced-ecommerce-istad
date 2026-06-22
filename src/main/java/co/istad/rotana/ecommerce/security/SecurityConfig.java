package co.istad.rotana.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

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
                .anyRequest().authenticated()
        );

        // 4. Disable CSRF token
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
