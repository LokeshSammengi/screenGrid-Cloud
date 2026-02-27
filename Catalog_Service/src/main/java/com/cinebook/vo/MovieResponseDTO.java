package com.cinebook.vo;



import lombok.Data;

@Data
public class MovieResponseDTO {

    private Long id;
    private String title;
    private String language;
    private Integer duration;
    private String genre;
}

