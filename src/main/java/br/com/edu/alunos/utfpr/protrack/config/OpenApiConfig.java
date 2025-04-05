package br.com.edu.alunos.utfpr.protrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Projeto Gestor de Projetos")
                .version("1.0")
                .description("API para gerenciamento de projetos, equipes, funcionários e clientes"));
    }
}
