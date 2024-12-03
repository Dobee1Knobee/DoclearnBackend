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


    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter @Setter
    @Column(name = "specialization", nullable = false)
    private String specialization;
    @Column(name = "last_name", nullable = false)
    @Getter @Setter
    private String lastName;
    @Column(name = "first_name", nullable = false)
    @Getter @Setter

    private String firstName;

    @Column(name = "password", nullable = false)

    private String password;


    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    @Column(name = "role")
    private UserRole role = UserRole.author;

    @Getter @Setter
    @Column(name ="email", unique = true)
    private String email;

    @Column(name = "birth_date")
    @Getter @Setter
    private LocalDate birthDate;

    @Column(name = "created_at", updatable = false)
    @Getter @Setter
    private LocalDate createdAt;

    @Getter
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    @Column(name = "rating", columnDefinition = "DOUBLE DEFAULT 0")
    @Getter
    private double rating;

    public Author(String firstName, String lastName, String password, String email, LocalDate birthDate,String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.setPassword(password);
        this.email = email;
        this.birthDate = birthDate;
        this.specialization = specialization;

    }

    protected Author() {

    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
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







}

