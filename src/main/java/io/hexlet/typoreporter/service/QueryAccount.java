package io.hexlet.typoreporter.service;

public interface QueryAccount {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
