package br.com.lucasbrum.booksapi.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDtos {

    public record Create(
            @NotBlank @Size(max = 150) String name
    ) {}

    public record Update(
            @NotBlank @Size(max = 150) String name
    ) {}

    public record Response(
            Long id,
            String name
    ) {}
}