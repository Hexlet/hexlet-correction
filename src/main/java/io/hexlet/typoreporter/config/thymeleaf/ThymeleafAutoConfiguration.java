package io.hexlet.typoreporter.config.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
// import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
public class ThymeleafAutoConfiguration {

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }
    // @Bean
    // public LayoutDialect layoutDialect() {
    //     return new LayoutDialect();
    // }

    // Make available Thymeleaf Spring Security Dialect on the templates
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
