package com.doclearn.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter@Getter
    private Long id;
    @Setter @Getter
    @Column(name = "specialization", nullable = false)
    private String specialization;
    @Column(name = "last_name", nullable = false)
    @Setter @Getter
    private String lastName;
    @Column(name = "first_name", nullable = false)
    @Setter @Getter
    private String firstName;

    @Column(name = "password", nullable = false)
    @Setter @Getter
    private String password;


    @Enumerated(EnumType.STRING)
    @Setter@Getter
    @Column(name = "role")
    private UserRole role = UserRole.author;

    @Setter @Getter
    @Column(name ="email", unique = true)
    private String email;

    @Column(name = "birth_date")
    @Setter @Getter
    private LocalDate birthDate;

    @Column(name = "created_at", updatable = false)
    @Setter @Getter
    private LocalDate createdAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @Setter @Getter
    private Set<Course> courses = new HashSet<>();

    @Column(name = "rating", columnDefinition = "DOUBLE DEFAULT 0")
    @Setter @Getter
    private double rating;

    public Author(String firstName, String lastName, String password, String email, LocalDate birthDate,String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.specialization = specialization;
        this.createdAt = LocalDate.now();
    }

    protected Author() {

    }


    public boolean checkPassword(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.password);
    }
    public void addCourse(Course course) {
        if(courses.contains(course)) {
            throw new IllegalArgumentException("Курс уже добавлен у автора.");
        }
        courses.add(course);
        updateRating();
    }



    public void updateRating() {
        if (courses.isEmpty()) {
            this.rating = 0.0;
        } else {
            this.rating = courses.stream()
                                 .mapToDouble(Course::getRating)
                                 .average()
                                 .orElse(0.0);
        }
    }


    public boolean isEnabled() {
        return false;
    }
}

