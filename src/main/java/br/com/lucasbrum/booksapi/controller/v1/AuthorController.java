package br.com.lucasbrum.booksapi.controller.v1;

import br.com.lucasbrum.booksapi.dto.v1.AuthorDtos;
import br.com.lucasbrum.booksapi.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Gerenciamento de autores")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    @Operation(summary = "Criar autor", description = "Cria um novo autor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Autor já existe"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<AuthorDtos.Response> create(@RequestBody @Valid AuthorDtos.Create dto) {
        var res = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar autor", description = "Atualiza um autor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<AuthorDtos.Response> update(
            @Parameter(description = "ID do autor") @PathVariable Long id, 
            @RequestBody @Valid AuthorDtos.Update dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar autor", description = "Remove um autor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID do autor") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID", description = "Retorna um autor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<AuthorDtos.Response> get(@Parameter(description = "ID do autor") @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Listar autores", description = "Retorna uma lista paginada de autores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de autores"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<AuthorDtos.Response>> list(
            @Parameter(description = "Filtrar por nome do autor") @RequestParam(value = "name", required = false) String name,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.list(name, pageable));
    }
}
