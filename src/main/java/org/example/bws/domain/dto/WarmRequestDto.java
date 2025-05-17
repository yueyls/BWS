package org.example.bws.domain.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WarmRequestDto {

    @NotNull
    private int carId;

    private int warnId;

    @NotNull
    private String signal;

}
