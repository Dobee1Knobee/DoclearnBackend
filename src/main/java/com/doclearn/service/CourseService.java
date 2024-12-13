package com.doclearn.service;

import com.doclearn.model.Course;
import com.doclearn.model.User;
import com.doclearn.repository.CourseRepository;
import com.doclearn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
        CourseRepository courseRepository;
    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
}
