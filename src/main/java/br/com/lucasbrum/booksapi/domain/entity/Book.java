package br.com.lucasbrum.booksapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book", uniqueConstraints = {
        @UniqueConstraint(name = "uq_book_title_author", columnNames = {"title","author_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_book_author"))
    private Author author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false, foreignKey = @ForeignKey(name = "fk_book_genre"))
    private Genre genre;
}
