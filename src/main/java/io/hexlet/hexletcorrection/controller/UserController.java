package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController implements FormValidator{

    private final UserService userService;

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) String name) {
        if (name == null) {
            return userService.findAll();
        }
        return userService.findByName(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
