
package com.cinebook.vo;

import java.util.List;
import lombok.Data;

@Data
public class TheatreResponseDTO {

    private Long id;
    private String name;
    private String location;

    private Long cityId;
    private String cityName;

    private List<ScreenResponseDTO> screens;
}
