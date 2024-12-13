package com.doclearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doclearn.model.Author;
import com.doclearn.model.Course;
@Repository
public interface CourseRepository  extends JpaRepository<Course,Long>{

	  // Метод для поиска курсов по имени категории
    List<Course> findByCategory_Name(String categoryName);
    
    // Метод для поиска курсов по автору
    List<Course> findByAuthor(Author author);
    
    // Метод для поиска курсов по ключевым словам в названии или описании
    List<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

}
