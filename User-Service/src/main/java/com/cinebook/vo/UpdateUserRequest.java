package com.cinebook.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest {

	@NotBlank
	private String name;
	@NotBlank
	private String email;
	@Pattern(regexp = "^[0-9]{10}$")
	private String mobile;
	
}
