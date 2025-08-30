package br.com.lucasbrum.booksapi.service;

import br.com.lucasbrum.booksapi.domain.entity.Author;
import br.com.lucasbrum.booksapi.domain.repository.AuthorRepository;
import br.com.lucasbrum.booksapi.dto.v1.AuthorDtos;
import br.com.lucasbrum.booksapi.exception.ConflictException;
import br.com.lucasbrum.booksapi.exception.NotFoundException;
import br.com.lucasbrum.booksapi.mapper.AuthorMapper;
import br.com.lucasbrum.booksapi.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceUnitTest {

    @Mock
    private AuthorRepository repository;
    
    @Mock
    private AuthorMapper mapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void create_WithValidAuthor_ShouldReturnAuthorResponse() {
        AuthorDtos.Create createDto = new AuthorDtos.Create("J.K. Rowling");
        Author author = Author.builder().id(1L).name("J.K. Rowling").build();
        AuthorDtos.Response responseDto = new AuthorDtos.Response(1L, "J.K. Rowling");

        when(repository.existsByNameIgnoreCase("J.K. Rowling")).thenReturn(false);
        when(mapper.toEntity(createDto)).thenReturn(author);
        when(repository.save(author)).thenReturn(author);
        when(mapper.toResponse(author)).thenReturn(responseDto);

        AuthorDtos.Response result = authorService.create(createDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("J.K. Rowling", result.name());
        verify(repository).save(any(Author.class));
    }

    @Test
    void create_WithDuplicateName_ShouldThrowConflictException() {
        AuthorDtos.Create createDto = new AuthorDtos.Create("J.K. Rowling");

        when(repository.existsByNameIgnoreCase("J.K. Rowling")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authorService.create(createDto));
        verify(repository, never()).save(any(Author.class));
    }

    @Test
    void getById_WithExistingAuthor_ShouldReturnAuthor() {
        Long authorId = 1L;
        Author author = Author.builder().id(authorId).name("J.K. Rowling").build();
        AuthorDtos.Response responseDto = new AuthorDtos.Response(authorId, "J.K. Rowling");

        when(repository.findById(authorId)).thenReturn(Optional.of(author));
        when(mapper.toResponse(author)).thenReturn(responseDto);

        AuthorDtos.Response result = authorService.getById(authorId);

        assertNotNull(result);
        assertEquals(authorId, result.id());
        assertEquals("J.K. Rowling", result.name());
    }

    @Test
    void getById_WithNonExistentAuthor_ShouldThrowNotFoundException() {
        Long authorId = 999L;

        when(repository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.getById(authorId));
    }

    @Test
    void delete_WithExistingAuthor_ShouldDeleteSuccessfully() {
        Long authorId = 1L;
        Author author = Author.builder().id(authorId).name("J.K. Rowling").build();

        when(repository.findById(authorId)).thenReturn(Optional.of(author));

        assertDoesNotThrow(() -> authorService.delete(authorId));
        verify(repository).delete(author);
    }

    @Test
    void delete_WithNonExistentAuthor_ShouldThrowNotFoundException() {
        Long authorId = 999L;

        when(repository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.delete(authorId));
    }
}