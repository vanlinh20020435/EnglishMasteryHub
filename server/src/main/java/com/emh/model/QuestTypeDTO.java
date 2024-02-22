package com.emh.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestTypeDTO {

    private Integer typeId;

    @NotNull
    @Size(max = 50)
    private String code;

    @NotNull
    @Size(max = 50)
    private String name;

}
