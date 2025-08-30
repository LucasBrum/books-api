package br.com.lucasbrum.booksapi.service;

import br.com.lucasbrum.booksapi.dto.v1.GenreDtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {
    GenreDtos.Response create(GenreDtos.Create dto);
    GenreDtos.Response update(Long id, GenreDtos.Update dto);
    void delete(Long id);
    GenreDtos.Response getById(Long id);
    Page<GenreDtos.Response> list(String name, Pageable pageable);
}
