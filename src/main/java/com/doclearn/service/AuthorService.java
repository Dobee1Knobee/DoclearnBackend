package com.doclearn.service;

import com.doclearn.model.Author;
import com.doclearn.model.User;
import com.doclearn.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

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
        if (authorRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
