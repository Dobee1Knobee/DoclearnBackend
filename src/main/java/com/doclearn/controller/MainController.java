package com.doclearn.controller;

import com.doclearn.model.Author;
import com.doclearn.model.User;
import com.doclearn.repository.AuthorRepository;
import com.doclearn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/secured")
public class MainController {

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;

    // Инъекция зависимостей через конструктор
    @Autowired
    public MainController(UserRepository userRepository, AuthorRepository authorRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/user")
    public User userAccess(Principal principal) {
        if (principal != null) {
            return userRepository.findByEmail(principal.getName()).orElse(null);
        }
        return null;
    }

    @GetMapping("/author")
    public Author authorAccess(Principal principal) {
        if (principal != null) {
            return authorRepository.findByEmail(principal.getName()).orElse(null);
        }
        return null;
    }
}
