package io.hexlet.typoreporter.domain.account;

public enum AuthProvider {

    EMAIL("EMAIL"), GITHUB("GITHUB"), GOOGLE("GOOGLE"), YANDEX("YANDEX");

    private String name;

    AuthProvider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static AuthProvider fromName(String name) {
        for (AuthProvider provider : values()) {
            if (provider.name.equals(name)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Provider not found: " + name);
    }
}
