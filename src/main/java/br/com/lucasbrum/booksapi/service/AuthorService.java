package br.com.lucasbrum.booksapi.service;

import br.com.lucasbrum.booksapi.dto.v1.AuthorDtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorDtos.Response create(AuthorDtos.Create dto);
    AuthorDtos.Response update(Long id, AuthorDtos.Update dto);
    void delete(Long id);
    AuthorDtos.Response getById(Long id);
    Page<AuthorDtos.Response> list(String name, Pageable pageable);
}
