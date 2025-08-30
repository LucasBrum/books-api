package br.com.lucasbrum.booksapi.dto.v1;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record LoginRequest(
            @NotBlank(message = "Username é obrigatório")
            @Size(min = 3, max = 100, message = "Username deve ter entre 3 e 100 caracteres")
            String username,
            
            @NotBlank(message = "Password é obrigatório")
            @Size(min = 6, max = 255, message = "Password deve ter pelo menos 6 caracteres")
            String password
    ) {}

    public record LoginResponse(
            String access_token,
            String token_type,
            Long expires_in,
            String username,
            String[] roles
    ) {}

    public record RegisterRequest(
            @NotBlank(message = "Username é obrigatório")
            @Size(min = 3, max = 100, message = "Username deve ter entre 3 e 100 caracteres")
            String username,
            
            @NotBlank(message = "Password é obrigatório")
            @Size(min = 6, max = 255, message = "Password deve ter pelo menos 6 caracteres")
            String password,
            
            @NotBlank(message = "Email é obrigatório")
            @Email(message = "Email deve ser válido")
            @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
            String email
    ) {}
}
