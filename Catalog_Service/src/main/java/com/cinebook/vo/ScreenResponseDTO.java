package com.cinebook.vo;

import java.util.List;
import lombok.Data;

@Data
public class ScreenResponseDTO {

    private Long id;
    private String name;
    private Integer totalSeats;

    //private TheatreResponseDTO theatre;(instead of this )
    private Long theatreId;
    private String theatreName;

    private List<ShowResponseDTO> shows;
}

