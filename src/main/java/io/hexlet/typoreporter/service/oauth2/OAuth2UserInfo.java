package io.hexlet.typoreporter.service.oauth2;

import java.util.Map;

public interface OAuth2UserInfo {
    String getEmail();
    String getUsername();
    String getFirstName();
    String getLastName();
    Map<String, Object> getAttributes();
}
