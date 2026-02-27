package com.cinebook.vo;


import lombok.Data;

@Data
public class ScreenSummaryDTO {

    private Long id;
    private String name;
    private Integer totalSeats;

    //private TheatreResponseDTO theatre;(instead of this )
    private Long theatreId;
    private String theatreName;


}

