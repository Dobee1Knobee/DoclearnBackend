package com.doclearn.controller;

import com.doclearn.model.Author;
import com.doclearn.model.Course;
import com.doclearn.repository.CourseRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseController {

    private CourseRepository courseRepository;
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostMapping("/save")
    private boolean SaveCourse(Course course, Author author) {
        if(author != null){
            course.setAuthor(author);
            courseRepository.save(course);
            return true;
        }
        return false;
    }

}
