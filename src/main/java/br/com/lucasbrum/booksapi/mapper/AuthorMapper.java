package br.com.lucasbrum.booksapi.mapper;

import br.com.lucasbrum.booksapi.domain.entity.Author;
import br.com.lucasbrum.booksapi.dto.v1.AuthorDtos;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    
    public Author toEntity(AuthorDtos.Create dto) {
        if (dto == null) {
            return null;
        }
        
        return Author.builder()
                .name(dto.name())
                .build();
    }
    
    public Author toEntity(AuthorDtos.Update dto) {
        if (dto == null) {
            return null;
        }
        
        return Author.builder()
                .name(dto.name())
                .build();
    }
    
    public AuthorDtos.Response toResponse(Author entity) {
        if (entity == null) {
            return null;
        }
        
        return new AuthorDtos.Response(
                entity.getId(),
                entity.getName()
        );
    }
}