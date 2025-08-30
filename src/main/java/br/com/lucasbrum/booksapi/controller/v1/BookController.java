package br.com.lucasbrum.booksapi.controller.v1;

import br.com.lucasbrum.booksapi.dto.v1.BookDtos;
import br.com.lucasbrum.booksapi.service.BookService;
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
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "Gerenciamento de livros")
@SecurityRequirement(name = "Bearer Authentication")
public class BookController {

    private final BookService service;

    @PostMapping
    @Operation(summary = "Criar livro", description = "Cria um novo livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Livro já existe"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<BookDtos.Response> create(@RequestBody @Valid BookDtos.Create dto) {
        var res = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza um livro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<BookDtos.Response> update(
            @Parameter(description = "ID do livro") @PathVariable Long id, 
            @RequestBody @Valid BookDtos.Update dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar livro", description = "Remove um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID do livro") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna um livro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<BookDtos.Response> get(@Parameter(description = "ID do livro") @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Listar livros", description = "Retorna uma lista paginada de livros com filtros opcionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de livros"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<BookDtos.Response>> list(
            @Parameter(description = "Filtrar por título do livro") @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "Filtrar por ID do autor") @RequestParam(value = "authorId", required = false) Long authorId,
            @Parameter(description = "Filtrar por ID do gênero") @RequestParam(value = "genreId", required = false) Long genreId,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(service.list(title, authorId, genreId, pageable));
    }
}
