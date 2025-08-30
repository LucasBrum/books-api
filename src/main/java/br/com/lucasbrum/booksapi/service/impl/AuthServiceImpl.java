package br.com.lucasbrum.booksapi.service.impl;

import br.com.lucasbrum.booksapi.domain.entity.User;
import br.com.lucasbrum.booksapi.dto.v1.AuthDtos;
import br.com.lucasbrum.booksapi.exception.UnauthorizedException;
import br.com.lucasbrum.booksapi.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final PasswordEncoder passwordEncoder;
    
    public AuthServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    // Usuários em memória para teste (em produção, use banco de dados)
    private final Map<String, User> usersInMemory = new HashMap<>();
    

    
    @PostConstruct
    private void createTestUsers() {
        // Usuário READER (só pode ler)
        User reader = User.builder()
                .id(1L)
                .username("reader")
                .password(passwordEncoder.encode("123456"))
                .email("reader@test.com")
                .active(true)
                .roles(Set.of(User.Role.READER))
                .build();
        
        // Usuário WRITER (pode ler e escrever)
        User writer = User.builder()
                .id(2L)
                .username("writer")
                .password(passwordEncoder.encode("123456"))
                .email("writer@test.com")
                .active(true)
                .roles(Set.of(User.Role.READER, User.Role.WRITER))
                .build();
        
        // Usuário ADMIN (pode tudo)
        User admin = User.builder()
                .id(3L)
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .email("admin@test.com")
                .active(true)
                .roles(Set.of(User.Role.READER, User.Role.WRITER))
                .build();
        
        usersInMemory.put("reader", reader);
        usersInMemory.put("writer", writer);
        usersInMemory.put("admin", admin);
    }

    @Override
    public AuthDtos.LoginResponse login(AuthDtos.LoginRequest request) {
        User user = usersInMemory.get(request.username());
        
        if (user == null || !user.getActive() || 
            !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }
        
        String token = generateToken(user);
        
        return new AuthDtos.LoginResponse(
                token,
                "Bearer",
                3600L,
                user.getUsername(),
                user.getRoles().stream().map(Enum::name).toArray(String[]::new)
        );
    }

    @Override
    public AuthDtos.LoginResponse register(AuthDtos.RegisterRequest request) {
        if (usersInMemory.containsKey(request.username())) {
            throw new UnauthorizedException("Username já existe");
        }
        
        User newUser = User.builder()
                .id((long) (usersInMemory.size() + 1))
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .active(true)
                .roles(Set.of(User.Role.READER)) // Usuários novos começam como READER
                .build();
        
        usersInMemory.put(request.username(), newUser);
        
        String token = generateToken(newUser);
        
        return new AuthDtos.LoginResponse(
                token,
                "Bearer",
                3600L,
                newUser.getUsername(),
                newUser.getRoles().stream().map(Enum::name).toArray(String[]::new)
        );
    }

    private String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(1, ChronoUnit.HOURS);
        
        // Garante que a chave tenha pelo menos 512 bits (64 bytes) para HS512
        byte[] secret = jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (secret.length < 64) {
            byte[] extendedSecret = new byte[64];
            System.arraycopy(secret, 0, extendedSecret, 0, Math.min(secret.length, 64));
            for (int i = secret.length; i < 64; i++) {
                extendedSecret[i] = secret[i % secret.length];
            }
            secret = extendedSecret;
        }
        
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .claim("userId", user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }
}
