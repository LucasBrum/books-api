package br.com.lucasbrum.booksapi.domain.entity;

import br.com.lucasbrum.booksapi.domain.entity.BaseAudit;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

// Temporariamente desabilitado para usar usuários em memória
// @Entity
// @Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @Column(unique = true, nullable = false, length = 100)
    private String username;
    
    // @Column(nullable = false, length = 255)
    private String password;
    
    // @Column(nullable = false, length = 100)
    private String email;
    
    // @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    // @ElementCollection(fetch = FetchType.EAGER)
    // @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    // @Column(name = "role", length = 50)
    // @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    
    public enum Role {
        READER,    // Pode ler (GET)
        WRITER     // Pode criar, atualizar e deletar (POST, PUT, DELETE)
    }
}
