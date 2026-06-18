package com.facultad.sistemaavisos.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI sistemaAvisosOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Avisos API")
                        .version("v1")
                        .description("Documentacion interactiva para probar los endpoints del sistema."));
    }
}
