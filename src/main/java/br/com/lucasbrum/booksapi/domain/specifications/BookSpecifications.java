package br.com.lucasbrum.booksapi.domain.specifications;

import br.com.lucasbrum.booksapi.domain.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecifications {

    private BookSpecifications() {}

    public static Specification<Book> titleContains(String title) {
        return (root, cq, cb) -> (title == null || title.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> hasAuthorId(Long authorId) {
        return (root, cq, cb) -> (authorId == null)
                ? cb.conjunction()
                : cb.equal(root.get("author").get("id"), authorId);
    }

    public static Specification<Book> hasGenreId(Long genreId) {
        return (root, cq, cb) -> (genreId == null)
                ? cb.conjunction()
                : cb.equal(root.get("genre").get("id"), genreId);
    }
}
