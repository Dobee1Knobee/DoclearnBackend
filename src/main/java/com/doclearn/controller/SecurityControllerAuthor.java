package com.doclearn.controller;

import com.doclearn.config.JwtCore;
import com.doclearn.model.Author;
import com.doclearn.repository.AuthorRepository;
import com.doclearn.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author/auth")
public class SecurityControllerAuthor {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final AuthorService authorService;

    @Autowired
    public SecurityControllerAuthor(AuthorRepository authorRepository, PasswordEncoder passwordEncoder,
                                    @Qualifier("authenticationManagerAuthor")
                                    AuthenticationManager authenticationManager, JwtCore jwtCore, AuthorService authorService) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.authorService = authorService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Author author) {
        Authentication authentication = null;

        System.out.println(author.getPassword());
        System.out.println(authorService.loadUserByUsername(author.getEmail()));
        System.out.println(passwordEncoder.encode(author.getPassword()));

        if (passwordEncoder.matches(author.getPassword(), authorService.loadUserByUsername(author.getEmail()).getPassword())) {
            System.out.println("Автор успешно аутентифицирован!");
        } else {
            System.out.println("Ошибка аутентификации автора!");
        }

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(author.getEmail(), author.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtCore.generateToken(authentication);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Author author) {
        // Проверяем, существует ли уже автор с таким email
        if (authorRepository.existsByEmail(author.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        // Валидация пароля (например, минимальная длина 8 символов)
        if (author.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long");
        }

        // Хэшируем пароль перед сохранением
        author.setPassword(passwordEncoder.encode(author.getPassword()));

        try {
            // Сохраняем нового автора в базе данных
            authorRepository.save(author);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving author: " + e.getMessage());
        }

        // Возвращаем успешный ответ с информацией о зарегистрированном авторе (например, email)
        return ResponseEntity.status(HttpStatus.CREATED).body("Author registered successfully with email: " + author.getEmail());
    }
}
