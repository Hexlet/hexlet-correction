package io.hexlet.typoreporter.service.oauth2;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

public class GithubOAuth2UserInfo implements OAuth2UserInfo {

    private final String accessToken;
    private final Map<String, Object> attributes;

    public GithubOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.attributes = attributes;
    }

    @Override
    public String getEmail() {
        var email = attributes.get("email");
        if (email == null) {
            WebClient webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

            List<Map<String, Object>> emails = webClient.get()
                .uri("/user/emails")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() { })
                .block();

            email = emails.stream()
                .filter(e -> Boolean.TRUE.equals(e.get("primary")))
                .map(e -> (String) e.get("email"))
                .findFirst()
                .orElse(null);
        }
        return (String) email;
    }

    @Override
    public String getUsername() {
        return attributes.get("login").toString();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getFirstName() {
        String[] names = attributes.get("name").toString().split(" ");
        return names.length > 0 ? names[0] : "";
    }

    @Override
    public String getLastName() {
        String[] names = attributes.get("name").toString().split(" ");
        return names.length > 1 ? names[1] : "";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getYandexId() {
        return "";
    }
}
