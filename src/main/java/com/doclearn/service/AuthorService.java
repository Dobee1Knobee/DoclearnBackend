package com.doclearn.service;

import com.doclearn.config.UserDetailsImpl;
import com.doclearn.model.Author;
import com.doclearn.model.User;
import com.doclearn.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("authorDetailsService")

public class AuthorService implements UserDetailsService {

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Сохраняет нового автора в базе данных, если email уникален.
     * @param author - объект пользователя для сохранения.
     * @return сохранённый автор.
     */
    public Author saveAuthor(Author author) {
        validateEmailUniqueness(author.getEmail());
        return authorRepository.save(author);
    }
    /**
     * Получает всех авторов .
     * @return список всех авторов.
     */
    public List<Author> getAll() {
        return authorRepository.findAll();
    }



    private void validateEmailUniqueness(String email) {
        if (authorRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    public UserDetails loadUserByUsername(String email) {
        Author author = authorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь с email %s не найден", email)));

        return UserDetailsImpl.buildAuthor(author);
    }
}
