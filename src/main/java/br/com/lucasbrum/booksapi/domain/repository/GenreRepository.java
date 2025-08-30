package br.com.lucasbrum.booksapi.domain.repository;

import br.com.lucasbrum.booksapi.domain.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Genre> findByNameIgnoreCase(String name);
}