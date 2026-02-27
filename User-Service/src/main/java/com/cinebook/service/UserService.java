package com.cinebook.service;

import java.util.List;

import com.cinebook.entities.LoginRequest;
import com.cinebook.vo.UpdateUserRequest;
import com.cinebook.vo.UserRequest;
import com.cinebook.vo.UserResponse;



public interface UserService {

	public UserResponse registerUser(UserRequest userrequest);
	public UserResponse getUserById(Long id);
	public List<UserResponse> getAllUsers();
	public List<UserResponse> getAllbystatus();
	public UserResponse updateUser(Long id,UpdateUserRequest updateuserrequest);
	public void deleteUserById(Long id);
	//sec
	public String login(LoginRequest request);
	
}
