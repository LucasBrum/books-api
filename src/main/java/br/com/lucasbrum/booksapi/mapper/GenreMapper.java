package br.com.lucasbrum.booksapi.mapper;

import br.com.lucasbrum.booksapi.domain.entity.Genre;
import br.com.lucasbrum.booksapi.dto.v1.GenreDtos;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    
    public Genre toEntity(GenreDtos.Create dto) {
        if (dto == null) {
            return null;
        }
        
        return Genre.builder()
                .name(dto.name())
                .build();
    }
    
    public Genre toEntity(GenreDtos.Update dto) {
        if (dto == null) {
            return null;
        }
        
        return Genre.builder()
                .name(dto.name())
                .build();
    }
    
    public GenreDtos.Response toResponse(Genre entity) {
        if (entity == null) {
            return null;
        }
        
        return new GenreDtos.Response(
                entity.getId(),
                entity.getName()
        );
    }
}