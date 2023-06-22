package com.spring.boot.application.config.springdoc;

import com.spring.boot.application.common.utils.Constant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {
    @Bean
    public OpenAPI applicationOpenAPI() {
        final String securitySchemeName = Constant.HEADER_TOKEN;
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                )
                )
                .info(info());
    }

    private Info info() {
        return new Info()
                .description("Documents with Swagger v3")
                .version("v1.0")
                .contact(
                        new Contact()
                                .name("Phan Quang Qui")
                                .email("pquangqui.it@gmail.com")
                );
    }
}
