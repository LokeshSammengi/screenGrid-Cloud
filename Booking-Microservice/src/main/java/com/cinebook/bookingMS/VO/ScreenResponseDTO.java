package com.cinebook.bookingMS.VO;

import java.util.List;
import lombok.Data;


public class ScreenResponseDTO {

    private Long id;
    private String name;
    private Integer totalSeats;

    //private TheatreResponseDTO theatre;(instead of this )
    private Long theatreId;
    private String theatreName;

   
}

