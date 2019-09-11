package io.hexlet.hexletcorrection.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket fullApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("full-api")
                .produces(Set.of(MediaType.APPLICATION_JSON_VALUE))
                .select()
                .apis(baseControllersPackage())
                .paths(PathSelectors.any())
                .build();
    }

    private Predicate<RequestHandler> baseControllersPackage() {
        return RequestHandlerSelectors.basePackage("io.hexlet.hexletcorrection.controller");
    }
}
