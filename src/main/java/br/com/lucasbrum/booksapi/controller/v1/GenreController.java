package br.com.lucasbrum.booksapi.controller.v1;

import br.com.lucasbrum.booksapi.dto.v1.GenreDtos;
import br.com.lucasbrum.booksapi.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
@Tag(name = "Gêneros", description = "Gerenciamento de gêneros literários")
@SecurityRequirement(name = "Bearer Authentication")
public class GenreController {

    private final GenreService service;

    @PostMapping
    @Operation(summary = "Criar gênero", description = "Cria um novo gênero literário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gênero criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Gênero já existe"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<GenreDtos.Response> create(@RequestBody @Valid GenreDtos.Create dto) {
        var res = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar gênero", description = "Atualiza um gênero existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gênero atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Gênero não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<GenreDtos.Response> update(
            @Parameter(description = "ID do gênero") @PathVariable Long id, 
            @RequestBody @Valid GenreDtos.Update dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar gênero", description = "Remove um gênero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Gênero removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Gênero não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID do gênero") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar gênero por ID", description = "Retorna um gênero específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gênero encontrado"),
            @ApiResponse(responseCode = "404", description = "Gênero não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<GenreDtos.Response> get(@Parameter(description = "ID do gênero") @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Listar gêneros", description = "Retorna uma lista paginada de gêneros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de gêneros"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<GenreDtos.Response>> list(
            @Parameter(description = "Filtrar por nome do gênero") @RequestParam(value = "name", required = false) String name,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.list(name, pageable));
    }
}
