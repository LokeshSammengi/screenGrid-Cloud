package com.cinebook.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CityRequestDTO {
	
	@NotBlank(message = "city name is required")
	private String name;

}
