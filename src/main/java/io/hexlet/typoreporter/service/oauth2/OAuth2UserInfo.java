package io.hexlet.typoreporter.service.oauth2;

import java.util.Map;

public interface OAuth2UserInfo {
    String getEmail();
    String getUsername();
    String getPassword();
    String getFirstName();
    String getLastName();
    String getYandexId();
    Map<String, Object> getAttributes();
}
