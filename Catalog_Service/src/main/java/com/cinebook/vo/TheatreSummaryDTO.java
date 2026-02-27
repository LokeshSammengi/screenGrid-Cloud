
package com.cinebook.vo;

import lombok.Data;

@Data
public class TheatreSummaryDTO {

    private Long id;
    private String name;
    private String location;

    private Long cityId;
    private String cityName;

  
}
