package io.hexlet.typoreporter.service.oauth2;

import java.util.Map;

public class YandexOAuth2UserInfo implements OAuth2UserInfo {

    private final String accessToken;
    private final Map<String, Object> attributes;

    public YandexOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.attributes = attributes;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("default_email");
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("login");
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("first_name");
    }

    @Override
    public String getLastName() {
        return (String) attributes.get("last_name");
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
