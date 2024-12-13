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
    @JoinColumn(name = "author_id", nullable = false)
    @Setter
    private Author author;

    @Getter @Setter
    private Double price;

    @Column(name = "sales_count", columnDefinition = "INT DEFAULT 0")
    @Getter
    private int salesCount;

    @Column(name = "favorites_count", columnDefinition = "INT DEFAULT 0")
    @Getter
    private int favoritesCount;

    @Column(name = "created_at", updatable = false)
    @Getter @Setter
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
    public Course(String title, String description, Double price, List<CourseTopic> courseTopics) {
        this.title = title;
        this.description = description;
        this.videoUrl = null; // Инициализация по умолчанию
        this.rating = 0.0;
        this.author = author;
//        this.category = category; TODO сделать категории
        this.price = price;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.courseTopics = courseTopics != null ? courseTopics : new ArrayList<>(); // Обработка null
    }

    // Конструктор по умолчанию
    protected Course() {

    }

    // Метод для автоматической установки createdAt перед сохранением
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = new Timestamp(System.currentTimeMillis());
        }
        if (this.updatedAt == null) {
            this.updatedAt = new Timestamp(System.currentTimeMillis());
        }
    }

}