package io.hexlet.typoreporter.domain.account;

public enum AuthProvider {

    EMAIL, GITHUB, GOOGLE, YANDEX;

    public static AuthProvider fromString(String value) {
        for (AuthProvider provider : AuthProvider.values()) {
            if (provider.name().equalsIgnoreCase(value)) {
                return provider;
            }
        }
        return null;
    }
}
