package com.emh.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;


@Getter
@Setter
public class StudentDTO {

    private Integer studentId;

    @NotNull
    @Size(max = 16)
    private String username;

    @Size(max = 50)
    private String email;

    @NotNull
    @Size(max = 32)
    private String password;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private OffsetDateTime lastLogin;

    @NotNull
    private Integer gender;

    @NotNull
    private Integer status;

    @Size(max = 255)
    private String avatar;

    @NotNull
    private LocalDate birthday;

    private Integer doingExam;

    private OffsetDateTime startingTime;

    @Size(max = 11)
    private String timeRemaining;

    @NotNull
    private Integer classs;

    @NotNull
    private Integer user;

}
