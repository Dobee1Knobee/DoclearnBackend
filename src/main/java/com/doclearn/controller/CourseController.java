package com.doclearn.controller;

import com.doclearn.model.Author;
import com.doclearn.model.Course;
import com.doclearn.repository.AuthorRepository;
import com.doclearn.repository.CategoryRepository;
import com.doclearn.repository.CourseRepository;
import com.doclearn.repository.UserRepository;
import com.doclearn.service.CourseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/course")
public class CourseController {

    private CourseRepository courseRepository;
    private AuthorRepository authorRepository;
    private CourseService courseService;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    public CourseController(CourseRepository courseRepository,AuthorRepository authorRepository,CourseService courseService) {
        this.courseRepository = courseRepository;
        this.authorRepository = authorRepository;
        this.courseService = courseService;
    }

    @PostMapping("/save")
    private Course SaveCourse(@RequestBody  Course course, Principal principal) {
        if (principal != null) {
            Optional<Author> authorOptional = authorRepository.findByEmail(principal.getName());

            if (authorOptional.isPresent()) {
                course.setAuthor(authorOptional.get());
                 return courseService.saveCourse(course);

            } else {
                // Логика на случай, если автор не найден
                throw new IllegalArgumentException("Author not found for email: " + principal.getName());
            }
        }
        return null;
    }

}
