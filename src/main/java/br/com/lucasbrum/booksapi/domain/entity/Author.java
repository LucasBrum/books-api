package br.com.lucasbrum.booksapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "author", uniqueConstraints = {
        @UniqueConstraint(name = "uq_author_name", columnNames = "name")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Author extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;
}
