package com.doclearn.model;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    private User user;

    @ManyToMany
    @JoinTable(name = "cart_courses",
               joinColumns = @JoinColumn(name = "cart_id"),
               inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    public Cart() {}

    public Cart(User user) {
        this.user = user;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
        
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

    public Set<Course> getCourses() {
        return courses;
    }
}
