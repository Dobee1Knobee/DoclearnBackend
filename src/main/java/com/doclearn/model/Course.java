package com.doclearn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String description;

    @Getter @Setter
    @Column(name = "video_url", nullable = true)
    private String videoUrl;

    @Getter 
    @Column(name = "rating", columnDefinition = "DOUBLE DEFAULT 0")
    private double rating;

    @ManyToOne(cascade = CascadeType.ALL)
    @Getter @Setter
    private Author author;

    @JoinColumn(name = "author_id", nullable = false)
    @Getter @Setter
    private Long authorId;


    @Getter @Setter
    private double price;

    @Column(name = "sales_count", columnDefinition = "INT DEFAULT 0")
    @Getter
    private int salesCount;

    @Column(name = "favorites_count", columnDefinition = "INT DEFAULT 0")
    @Getter
    private int favoritesCount;

    @Column(name = "created_at", updatable = false)
    @Getter
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @Getter @Setter
    private Timestamp updatedAt;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Getter @Setter
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTopic> courseTopics = new ArrayList<>();

    // Конструктор с параметрами
    public Course(String title, String description, Author author, double price, Category category, List<CourseTopic> courseTopics) {
        this.title = title;
        this.description = description;
        this.videoUrl = null; // Инициализация по умолчанию
        this.rating = 0.0; 
        this.author = author;
        this.authorId = author.getId();
        this.category = category;
        this.price = price;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.courseTopics = courseTopics != null ? courseTopics : new ArrayList<>(); // Обработка null
    }

    // Конструктор по умолчанию
    public Course() {
        this.courseTopics = new ArrayList<>(); // Инициализация пустого списка
        this.createdAt = new Timestamp(System.currentTimeMillis()); // Установка времени создания по умолчанию
    }

    public void setRating(double v) {
    }
}

