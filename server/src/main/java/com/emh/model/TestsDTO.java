package com.emh.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class TestsDTO {

    private Integer testCode;

    @NotNull
    @Size(max = 255)
    private String testName;

    @NotNull
    @Size(max = 32)
    private String password;

    private Integer subjectId;

    @NotNull
    private Integer gradeId;

    @NotNull
    private Integer totalQuestions;

    @NotNull
    private Integer timeToDo;

    @NotNull
    private String note;

    @NotNull
    private Integer status;

    @NotNull
    private OffsetDateTime timest;

}
