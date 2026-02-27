package com.cinebook.vo;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ShowResponseDTO {

    private Long id;

    private Long movieId;
    private String movieTitle;

    private Long screenId;
    private String screenName;

    private LocalDate showDate;
    private LocalTime showTime;

    private Double price;
}

