package crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for Swagger documentation.
 * Enables Swagger2 for API documentation generation.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Configures and returns a Docket bean for Swagger API documentation.
     *
     * @return Docket bean for Swagger API documentation.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("crud.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
