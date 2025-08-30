package br.com.lucasbrum.booksapi.service;

import br.com.lucasbrum.booksapi.dto.v1.BookDtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDtos.Response create(BookDtos.Create dto);
    BookDtos.Response update(Long id, BookDtos.Update dto);
    void delete(Long id);
    BookDtos.Response getById(Long id);
    Page<BookDtos.Response> list(String title, Long authorId, Long genreId, Pageable pageable);
}
