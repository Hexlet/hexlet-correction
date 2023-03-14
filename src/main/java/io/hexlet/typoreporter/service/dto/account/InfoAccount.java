package io.hexlet.typoreporter.service.dto.account;

public record InfoAccount(
    Long id,
    String email,
    String username,
    String firstName,
    String lastName
) {}
