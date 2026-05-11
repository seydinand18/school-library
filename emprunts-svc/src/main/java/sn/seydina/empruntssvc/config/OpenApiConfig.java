package sn.seydina.empruntssvc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Emprunts Service API")
                        .description("Gestion des prêts et retours de livres")
                        .version("1.0.0"))
                .servers(List.of(new Server().url("http://localhost:8083").description("emprunts-svc")));
    }
}
