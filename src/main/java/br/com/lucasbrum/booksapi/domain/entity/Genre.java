package br.com.lucasbrum.booksapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genre", uniqueConstraints = {
        @UniqueConstraint(name = "uq_genre_name", columnNames = "name")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Genre extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
}