package com.emh.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;


@Getter
@Setter
public class AdminDTO {

    private Integer adminId;

    @NotNull
    @Size(max = 16)
    private String username;

    @Size(max = 50)
    private String email;

    @Size
    private String password;

    @NotNull
    @Size(max = 50)
    private String name;

    private OffsetDateTime lastLogin;

    private Integer gender;

    private Integer status;

    @Size(max = 255)
    private String avatar;

    private LocalDate birthday;

    private Integer user;

}
