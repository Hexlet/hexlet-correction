package io.hexlet.typoreporter.config.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.github")
@Getter
@Setter
public class OAuth2ConfigurationProperties {
    @Value("clientId")
    private String clientId;
    @Value("clientSecret")
    private String clientSecret;
    @Value("scope")
    private HashSet<String> scope;
}

