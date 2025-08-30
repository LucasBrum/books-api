package br.com.lucasbrum.booksapi.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenreDtos {

    public record Create(
            @NotBlank @Size(max = 100) String name
    ) {}

    public record Update(
            @NotBlank @Size(max = 100) String name
    ) {}

    public record Response(
            Long id,
            String name
    ) {}
}
