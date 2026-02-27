package com.cinebook.vo;

import com.cinebook.entities.UserRole;
import com.cinebook.entities.UserStatus;

import lombok.Data;

@Data
public class UserResponse {

	private Long id;
	private String name;
	private String email;
	private String mobile;
	private UserRole role;
	private UserStatus status;
}
