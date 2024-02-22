package com.emh.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClassesDTO {

    private Integer classId;

    @NotNull
    @Size(max = 50)
    private String className;

    @NotNull
    private Integer teacher;

}
