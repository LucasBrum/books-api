package br.com.lucasbrum.booksapi.dto.v1;

import jakarta.validation.constraints.*;

public class BookDtos {

    public record Create(
            @NotBlank @Size(max = 200) String title,
            @Min(0) @Max(3000) Integer publicationYear,
            @NotNull Long authorId,
            @NotNull Long genreId
    ) {}

    public record Update(
            @NotBlank @Size(max = 200) String title,
            @Min(0) @Max(3000) Integer publicationYear,
            @NotNull Long authorId,
            @NotNull Long genreId
    ) {}

    public record Response(
            Long id,
            String title,
            Integer publicationYear,
            Long authorId,
            String authorName,
            Long genreId,
            String genreName
    ) {}
}