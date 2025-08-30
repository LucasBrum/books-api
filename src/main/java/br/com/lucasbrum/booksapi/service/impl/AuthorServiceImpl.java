package br.com.lucasbrum.booksapi.service.impl;

import br.com.lucasbrum.booksapi.domain.entity.Author;
import br.com.lucasbrum.booksapi.domain.repository.AuthorRepository;
import br.com.lucasbrum.booksapi.dto.v1.AuthorDtos;
import br.com.lucasbrum.booksapi.exception.ConflictException;
import br.com.lucasbrum.booksapi.exception.NotFoundException;
import br.com.lucasbrum.booksapi.mapper.AuthorMapper;
import br.com.lucasbrum.booksapi.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    @Override
    @Transactional
    public AuthorDtos.Response create(AuthorDtos.Create dto) {
        if (repository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException("Author name already exists: " + dto.name());
        }
        Author entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public AuthorDtos.Response update(Long id, AuthorDtos.Update dto) {
        Author entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));

        if (!entity.getName().equalsIgnoreCase(dto.name())
                && repository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException("Author name already exists: " + dto.name());
        }
        entity.setName(dto.name());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));
        repository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDtos.Response getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorDtos.Response> list(String name, Pageable pageable) {
        Page<Author> page = (name == null || name.isBlank())
                ? repository.findAll(pageable)
                : repository.findAll(Example.of(Author.builder().name(name).build(),
                ExampleMatcher.matching().withIgnoreCase().withMatcher("name",
                        ExampleMatcher.GenericPropertyMatchers.contains())), pageable);
        return page.map(mapper::toResponse);
    }
}
