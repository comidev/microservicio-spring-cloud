package comidev.customerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicios API")
                        .description("API Rest de Microservicios con Spring Cloud")
                        .contact(new Contact()
                                .name("Omar Miranda")
                                .email("comidev.contacto@gmail.com")
                                .url("https://comidev.vercel.app"))
                        .version("v0.0.1")
                        .license(new License()
                                .name("comidev")
                                .url("https://comidev.vercel.app")));
    }
}
