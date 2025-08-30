package br.com.lucasbrum.booksapi.service.impl;

import br.com.lucasbrum.booksapi.domain.entity.Genre;
import br.com.lucasbrum.booksapi.domain.repository.GenreRepository;
import br.com.lucasbrum.booksapi.dto.v1.GenreDtos;
import br.com.lucasbrum.booksapi.exception.ConflictException;
import br.com.lucasbrum.booksapi.exception.NotFoundException;
import br.com.lucasbrum.booksapi.mapper.GenreMapper;
import br.com.lucasbrum.booksapi.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;
    private final GenreMapper mapper;

    @Override
    @Transactional
    public GenreDtos.Response create(GenreDtos.Create dto) {
        if (repository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException("Genre name already exists: " + dto.name());
        }
        Genre entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public GenreDtos.Response update(Long id, GenreDtos.Update dto) {
        Genre entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found: " + id));

        if (!entity.getName().equalsIgnoreCase(dto.name())
                && repository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException("Genre name already exists: " + dto.name());
        }
        entity.setName(dto.name());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Genre entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found: " + id));
        repository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDtos.Response getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Genre not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenreDtos.Response> list(String name, Pageable pageable) {
        Page<Genre> page = (name == null || name.isBlank())
                ? repository.findAll(pageable)
                : repository.findAll(Example.of(Genre.builder().name(name).build(),
                ExampleMatcher.matching().withIgnoreCase().withMatcher("name",
                        ExampleMatcher.GenericPropertyMatchers.contains())), pageable);
        return page.map(mapper::toResponse);
    }
}