package com.emh.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Integer userId;

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

    private Integer status;

    @NotNull
    @Size(max = 50)
    private String role;

}
