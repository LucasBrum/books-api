package br.com.lucasbrum.booksapi.domain.repository;

import br.com.lucasbrum.booksapi.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsByTitleIgnoreCaseAndAuthor_Id(String title, Long authorId);
}
