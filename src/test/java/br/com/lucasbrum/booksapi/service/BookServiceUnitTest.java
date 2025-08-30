package br.com.lucasbrum.booksapi.service;

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
import br.com.lucasbrum.booksapi.service.impl.BookServiceImpl;
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
class BookServiceUnitTest {

    @Mock
    private BookRepository repository;
    
    @Mock
    private AuthorRepository authorRepository;
    
    @Mock
    private GenreRepository genreRepository;
    
    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void create_WithValidBook_ShouldReturnBookResponse() {
        BookDtos.Create createDto = new BookDtos.Create("Harry Potter", 1997, 1L, 1L);
        
        Author author = Author.builder().id(1L).name("J.K. Rowling").build();
        Genre genre = Genre.builder().id(1L).name("Fantasy").build();
        Book book = Book.builder().id(1L).title("Harry Potter").publicationYear(1997)
                .author(author).genre(genre).build();
        BookDtos.Response responseDto = new BookDtos.Response(
                1L, "Harry Potter", 1997, 1L, "J.K. Rowling", 1L, "Fantasy");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(repository.existsByTitleIgnoreCaseAndAuthor_Id("Harry Potter", 1L)).thenReturn(false);
        when(repository.save(any(Book.class))).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(responseDto);

        BookDtos.Response result = bookService.create(createDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Harry Potter", result.title());
        assertEquals("J.K. Rowling", result.authorName());
        assertEquals("Fantasy", result.genreName());
        verify(repository).save(any(Book.class));
    }

    @Test
    void create_WithNonExistentAuthor_ShouldThrowNotFoundException() {
        BookDtos.Create createDto = new BookDtos.Create("Harry Potter", 1997, 999L, 1L);

        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.create(createDto));
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void create_WithNonExistentGenre_ShouldThrowNotFoundException() {
        BookDtos.Create createDto = new BookDtos.Create("Harry Potter", 1997, 1L, 999L);
        Author author = Author.builder().id(1L).name("J.K. Rowling").build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.create(createDto));
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void create_WithDuplicateTitle_ShouldThrowConflictException() {
        BookDtos.Create createDto = new BookDtos.Create("Harry Potter", 1997, 1L, 1L);
        
        Author author = Author.builder().id(1L).name("J.K. Rowling").build();
        Genre genre = Genre.builder().id(1L).name("Fantasy").build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(repository.existsByTitleIgnoreCaseAndAuthor_Id("Harry Potter", 1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> bookService.create(createDto));
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void getById_WithExistingBook_ShouldReturnBook() {
        Long bookId = 1L;
        Author author = Author.builder().id(1L).name("J.K. Rowling").build();
        Genre genre = Genre.builder().id(1L).name("Fantasy").build();
        Book book = Book.builder().id(bookId).title("Harry Potter").publicationYear(1997)
                .author(author).genre(genre).build();
        BookDtos.Response responseDto = new BookDtos.Response(
                bookId, "Harry Potter", 1997, 1L, "J.K. Rowling", 1L, "Fantasy");

        when(repository.findById(bookId)).thenReturn(Optional.of(book));
        when(mapper.toResponse(book)).thenReturn(responseDto);

        BookDtos.Response result = bookService.getById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.id());
        assertEquals("Harry Potter", result.title());
    }

    @Test
    void getById_WithNonExistentBook_ShouldThrowNotFoundException() {
        Long bookId = 999L;

        when(repository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.getById(bookId));
    }

    @Test
    void delete_WithExistingBook_ShouldDeleteSuccessfully() {
        Long bookId = 1L;
        Author author = Author.builder().id(1L).name("J.K. Rowling").build();
        Genre genre = Genre.builder().id(1L).name("Fantasy").build();
        Book book = Book.builder().id(bookId).title("Harry Potter").publicationYear(1997)
                .author(author).genre(genre).build();

        when(repository.findById(bookId)).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> bookService.delete(bookId));
        verify(repository).delete(book);
    }

    @Test
    void delete_WithNonExistentBook_ShouldThrowNotFoundException() {
        Long bookId = 999L;

        when(repository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.delete(bookId));
    }
}