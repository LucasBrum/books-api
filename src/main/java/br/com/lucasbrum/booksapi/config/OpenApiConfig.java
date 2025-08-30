package br.com.lucasbrum.booksapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Books API")
                        .description("""
                                ## 📚 Books API - Sistema de Gerenciamento de Biblioteca
                                
                                API REST completa para gerenciamento de livros, autores e gêneros com autenticação JWT.
                                
                                ### 🔐 **Como Testar:**
                                1. Faça login no endpoint `/auth/login` com as credenciais abaixo
                                2. Copie o `access_token` da resposta  
                                3. Clique no botão **Authorize** 🔒 no topo desta página
                                4. Cole o token (sem prefixo "Bearer")
                                5. Teste os endpoints protegidos
                                
                                ### 👥 **Usuários de Teste:**
                                - **admin** / **123456** - Permissões completas (READER + WRITER)
                                - **writer** / **123456** - Pode ler e escrever (READER + WRITER)  
                                - **reader** / **123456** - Apenas leitura (READER)
                                
                                ### 🛡️ **Permissões:**
                                - **READER**: GET em todos os endpoints
                                - **WRITER**: Todas as operações CRUD
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Lucas Brum")
                                .email("lucas@example.com")
                                .url("https://github.com/lucasbrum"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.books.com")
                                .description("Servidor de Produção")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
