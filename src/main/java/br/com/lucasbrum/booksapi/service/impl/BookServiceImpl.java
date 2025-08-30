package br.com.lucasbrum.booksapi.service.impl;

import br.com.lucasbrum.booksapi.domain.entity.Author;
import br.com.lucasbrum.booksapi.domain.entity.Book;
import br.com.lucasbrum.booksapi.domain.entity.Genre;
import br.com.lucasbrum.booksapi.domain.repository.AuthorRepository;
import br.com.lucasbrum.booksapi.domain.repository.BookRepository;
import br.com.lucasbrum.booksapi.domain.repository.GenreRepository;
import br.com.lucasbrum.booksapi.dto.v1.BookDtos;
import br.com.lucasbrum.booksapi.exception.ConflictException;
import br.com.lucasbrum.booksapi.exception.NotFoundException;
import br.com.lucasbrum.booksapi.mapper.BookMapper;
import br.com.lucasbrum.booksapi.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.lucasbrum.booksapi.domain.specifications.BookSpecifications.*;

@Service @RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper mapper;

    @Override
    @Transactional
    public BookDtos.Response create(BookDtos.Create dto) {
        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new NotFoundException("Author not found: " + dto.authorId()));
        Genre genre = genreRepository.findById(dto.genreId())
                .orElseThrow(() -> new NotFoundException("Genre not found: " + dto.genreId()));

        if (repository.existsByTitleIgnoreCaseAndAuthor_Id(dto.title(), dto.authorId())) {
            throw new ConflictException("Book with same title already exists for this author.");
        }

        Book entity = Book.builder()
                .title(dto.title())
                .publicationYear(dto.publicationYear())
                .author(author)
                .genre(genre)
                .build();

        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public BookDtos.Response update(Long id, BookDtos.Update dto) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new NotFoundException("Author not found: " + dto.authorId()));
        Genre genre = genreRepository.findById(dto.genreId())
                .orElseThrow(() -> new NotFoundException("Genre not found: " + dto.genreId()));

        if (!entity.getTitle().equalsIgnoreCase(dto.title())
                || !entity.getAuthor().getId().equals(dto.authorId())) {
            if (repository.existsByTitleIgnoreCaseAndAuthor_Id(dto.title(), dto.authorId())) {
                throw new ConflictException("Book with same title already exists for this author.");
            }
        }

        entity.setTitle(dto.title());
        entity.setPublicationYear(dto.publicationYear());
        entity.setAuthor(author);
        entity.setGenre(genre);

        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        repository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDtos.Response getById(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDtos.Response> list(String title, Long authorId, Long genreId, Pageable pageable) {
        var spec = Specification.where(titleContains(title))
                .and(hasAuthorId(authorId))
                .and(hasGenreId(genreId));
        return repository.findAll(spec, pageable).map(mapper::toResponse);
    }
}