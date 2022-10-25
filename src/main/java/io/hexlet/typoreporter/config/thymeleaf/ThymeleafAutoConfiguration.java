package io.hexlet.typoreporter.config.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class ThymeleafAutoConfiguration {

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    // Make available Thymeleaf Spring Security Dialect on the templates
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
