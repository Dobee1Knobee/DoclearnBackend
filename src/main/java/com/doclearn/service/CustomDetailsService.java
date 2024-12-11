package com.doclearn.service;

import com.doclearn.model.Author;
import com.doclearn.model.User;
import com.doclearn.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomDetailsService implements UserDetailsService {

    private final UserService userService;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    @Autowired
    public CustomDetailsService(@Qualifier("userService") UserService userService,
                                @Qualifier("authorService") AuthorService authorService,
                                AuthorRepository authorRepository) {
        this.userService = userService;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Логика для выбора сервиса на основе роли пользователя
        // Например, если роль "author", используем AuthorService, иначе UserService
//        if (isAuthor(username)) {
//            return authorService.loadUserByUsername(username);  // В случае с автором
//        } else {
//            return userService.loadUserByUsername(username);  // В случае с обычным пользователем
//        }

        //TODO fix the temporary method
        if (authorService.loadUserByUsername(username) != null) {
            return  authorService.loadUserByUsername(username);
        } else {
            return userService.loadUserByUsername(username);
        }

    }

    // Метод для определения роли пользователя (можно изменить в зависимости от вашей логики)
    private boolean isAuthor(String email) {
        // Логика для определения роли, например, через базу данных или с помощью JWT
        return authorRepository.findByEmail(email)
                .map(Author::isEnabled)  // Проверяем, что автор существует и активен
                .orElse(false);  // Если автор не найден, возвращаем false
    }
}