package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    List<User> findByName(String name);

    List<User> findAll();

    User create(User user);

    void delete(Long id);
}
