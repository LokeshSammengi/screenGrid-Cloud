package com.cinebook.vo;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TheatreRequestDTO {

    @NotBlank(message = "Theatre name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "City id is required")
    private Long cityId;
    
    
}


