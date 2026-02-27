package com.cinebook.vo;

import com.cinebook.entities.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequest {

	@NotBlank(message = "Name is required")
	private String name;
	
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;
	
	@Pattern(regexp = "^[0-9]{10}$", message="Mobile is required")
	private String mobile;
	
	@NotNull(message = "User role must be specified")
	private UserRole role;
	
	@NotBlank(message = "Password is required")
	private String password;

}
