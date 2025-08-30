package br.com.lucasbrum.booksapi.service;

import br.com.lucasbrum.booksapi.domain.entity.Genre;
import br.com.lucasbrum.booksapi.domain.repository.GenreRepository;
import br.com.lucasbrum.booksapi.dto.v1.GenreDtos;
import br.com.lucasbrum.booksapi.exception.ConflictException;
import br.com.lucasbrum.booksapi.exception.NotFoundException;
import br.com.lucasbrum.booksapi.mapper.GenreMapper;
import br.com.lucasbrum.booksapi.service.impl.GenreServiceImpl;
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
class GenreServiceUnitTest {

    @Mock
    private GenreRepository repository;
    
    @Mock
    private GenreMapper mapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    void create_WithValidGenre_ShouldReturnGenreResponse() {
        GenreDtos.Create createDto = new GenreDtos.Create("Fantasy");
        Genre genre = Genre.builder().id(1L).name("Fantasy").build();
        GenreDtos.Response responseDto = new GenreDtos.Response(1L, "Fantasy");

        when(repository.existsByNameIgnoreCase("Fantasy")).thenReturn(false);
        when(mapper.toEntity(createDto)).thenReturn(genre);
        when(repository.save(genre)).thenReturn(genre);
        when(mapper.toResponse(genre)).thenReturn(responseDto);

        GenreDtos.Response result = genreService.create(createDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Fantasy", result.name());
        verify(repository).save(any(Genre.class));
    }

    @Test
    void create_WithDuplicateName_ShouldThrowConflictException() {
        GenreDtos.Create createDto = new GenreDtos.Create("Fantasy");

        when(repository.existsByNameIgnoreCase("Fantasy")).thenReturn(true);

        assertThrows(ConflictException.class, () -> genreService.create(createDto));
        verify(repository, never()).save(any(Genre.class));
    }

    @Test
    void getById_WithExistingGenre_ShouldReturnGenre() {
        Long genreId = 1L;
        Genre genre = Genre.builder().id(genreId).name("Fantasy").build();
        GenreDtos.Response responseDto = new GenreDtos.Response(genreId, "Fantasy");

        when(repository.findById(genreId)).thenReturn(Optional.of(genre));
        when(mapper.toResponse(genre)).thenReturn(responseDto);

        GenreDtos.Response result = genreService.getById(genreId);

        assertNotNull(result);
        assertEquals(genreId, result.id());
        assertEquals("Fantasy", result.name());
    }

    @Test
    void getById_WithNonExistentGenre_ShouldThrowNotFoundException() {
        Long genreId = 999L;

        when(repository.findById(genreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.getById(genreId));
    }

    @Test
    void delete_WithExistingGenre_ShouldDeleteSuccessfully() {
        Long genreId = 1L;
        Genre genre = Genre.builder().id(genreId).name("Fantasy").build();

        when(repository.findById(genreId)).thenReturn(Optional.of(genre));

        assertDoesNotThrow(() -> genreService.delete(genreId));
        verify(repository).delete(genre);
    }

    @Test
    void delete_WithNonExistentGenre_ShouldThrowNotFoundException() {
        Long genreId = 999L;

        when(repository.findById(genreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.delete(genreId));
    }
}