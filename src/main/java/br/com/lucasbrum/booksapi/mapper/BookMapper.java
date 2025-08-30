package br.com.lucasbrum.booksapi.mapper;

import br.com.lucasbrum.booksapi.domain.entity.Book;
import br.com.lucasbrum.booksapi.dto.v1.BookDtos;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDtos.Response toResponse(Book entity) {
        if (entity == null) {
            return null;
        }
        
        return new BookDtos.Response(
                entity.getId(),
                entity.getTitle(),
                entity.getPublicationYear(),
                entity.getAuthor() != null ? entity.getAuthor().getId() : null,
                entity.getAuthor() != null ? entity.getAuthor().getName() : null,
                entity.getGenre() != null ? entity.getGenre().getId() : null,
                entity.getGenre() != null ? entity.getGenre().getName() : null
        );
    }
}
