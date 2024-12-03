package com.doclearn.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "temporary_regist" )
public class TemporaryRegToDel {
    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "email", nullable = false,unique = true)
    private String email;

    @Getter
    @Setter
    @Column(name = "specialization")
    private String specialization;

    @Getter
    @Setter
    @Column(name = "role",nullable = false)
    private String role;

    public TemporaryRegToDel(String email, String specialization, String code,String role) {
        this.email = email;
        this.specialization = specialization;
        this.role = role;

    }

    public TemporaryRegToDel() {

    }
}

