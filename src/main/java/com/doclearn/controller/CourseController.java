package com.doclearn.controller;

import com.doclearn.model.Author;
import com.doclearn.model.Course;
import com.doclearn.repository.AuthorRepository;
import com.doclearn.repository.CategoryRepository;
import com.doclearn.repository.CourseRepository;
import com.doclearn.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/course")
public class CourseController {

    private CourseRepository courseRepository;
    private AuthorRepository authorRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    public CourseController(CourseRepository courseRepository,AuthorRepository authorRepository) {
        this.courseRepository = courseRepository;
        this.authorRepository = authorRepository;
    }

    @PostMapping("/save")
    private boolean SaveCourse(Course course, Principal principal) {
        if (principal != null) {
            Optional<Author> authorOptional = authorRepository.findByEmail(principal.getName());

            if (authorOptional.isPresent()) {
                course.setAuthor(authorOptional.get());
                courseRepository.save(course);
                return true;
            } else {
                // Логика на случай, если автор не найден
                throw new IllegalArgumentException("Author not found for email: " + principal.getName());
            }
        }
        return false;
    }

}
