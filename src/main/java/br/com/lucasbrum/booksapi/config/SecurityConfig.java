package br.com.lucasbrum.booksapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret:}")
    private String jwtSecret;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                // Swagger + actuator health sempre públicos para facilitar testes
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                
                // TEMPORÁRIO: Liberando todos os endpoints para testes
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/v1/**").permitAll()
                
                // Endpoints originais com autenticação (comentados temporariamente)
                // .requestMatchers(HttpMethod.GET, "/api/v1/**").hasAnyRole("READER", "WRITER")
                // .requestMatchers(HttpMethod.POST, "/api/v1/**").hasRole("WRITER")
                // .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("WRITER")
                // .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("WRITER")

                .anyRequest().permitAll()
        );

        // TEMPORÁRIO: OAuth2 Resource Server desabilitado para testes
        // http.oauth2ResourceServer(oauth2 -> oauth2
        //         .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        // );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Extrair roles do claim "roles"
            Collection<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                return List.of();
            }
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        });
        return converter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        // HS512 para compatibilidade com JJWT
        // Garante que a chave tenha pelo menos 512 bits (64 bytes) para HS512
        byte[] secret = jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (secret.length < 64) {
            // Se a chave for muito curta, estende para 64 bytes para HS512
            byte[] extendedSecret = new byte[64];
            System.arraycopy(secret, 0, extendedSecret, 0, Math.min(secret.length, 64));
            // Preenche o resto com a chave original repetida
            for (int i = secret.length; i < 64; i++) {
                extendedSecret[i] = secret[i % secret.length];
            }
            secret = extendedSecret;
        }
        
        // Cria SecretKeySpec para HS512
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
    }
}