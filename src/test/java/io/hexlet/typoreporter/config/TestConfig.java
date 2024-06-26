package io.hexlet.typoreporter.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TestConfig {
    @Bean
    public Faker getFaker() {
        return new Faker();
    }
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
