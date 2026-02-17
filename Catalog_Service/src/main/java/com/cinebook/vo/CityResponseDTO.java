package com.cinebook.vo;

import lombok.Data;
import java.util.List;

@Data
public class CityResponseDTO {

	private Long id;
	private String name;
	private List<TheatreInCityDTO> theatres;
	
}
