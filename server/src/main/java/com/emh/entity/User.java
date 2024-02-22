package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, length = 16)
    private String username;

    @Column(length = 50)
    private String email;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false, name = "\"role\"", length = 50)
    private String role;

    @OneToMany(mappedBy = "user")
    private Set<Admin> userAdmins;

    @OneToMany(mappedBy = "user")
    private Set<Teacher> userTeachers;

    @OneToMany(mappedBy = "user")
    private Set<Student> userStudents;

}
