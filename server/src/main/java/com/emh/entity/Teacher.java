package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
public class Teacher {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teacherId;

    @Column(nullable = false, length = 16)
    private String username;

    @Column(length = 50)
    private String email;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private OffsetDateTime lastLogin;

    @Column(nullable = false)
    private Integer gender;

    @Column(nullable = false)
    private Integer status;

    @Column
    private String avatar;

    @Column(nullable = false)
    private LocalDate birthday;

    @OneToMany(mappedBy = "teacher")
    private Set<Classes> teacherClasseses;

    @OneToMany(mappedBy = "teacher")
    private Set<TeacherNotifications> teacherTeacherNotificationses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
