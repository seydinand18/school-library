package sn.seydina.livressvc.config;

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
                        .title("Livres Service API")
                        .description("Gestion du catalogue de livres et des exemplaires")
                        .version("1.0.0"))
                .servers(List.of(new Server().url("http://localhost:8082").description("livres-svc")));
    }
}
