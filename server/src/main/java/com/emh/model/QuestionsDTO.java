package com.emh.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestionsDTO {

    private Integer questionId;

    @NotNull
    private Integer unit;

    @NotNull
    private Integer levelId;

    @NotNull
    private String questionContent;

    @NotNull
    private String correctAnswer;

    @NotNull
    @Size(max = 255)
    private String sentBy;

    @NotNull
    private Integer status;

    @NotNull
    private Integer type;

}
