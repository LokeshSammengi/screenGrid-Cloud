package com.cinebook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinebook.entities.LoginRequest;
import com.cinebook.service.UserService;
import com.cinebook.vo.UpdateUserRequest;
import com.cinebook.vo.UserRequest;
import com.cinebook.vo.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

//	@Autowired
	private final UserService userservice;
	
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest request) {
	    return userservice.login(request);
	}

	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> registeruser(@Valid @RequestBody UserRequest userrequest){
		UserResponse userresponse = userservice.registerUser(userrequest);
		return new ResponseEntity<UserResponse>(userresponse,HttpStatus.OK);
	}
	@PreAuthorize("hasAnyRole('THEATRE_ADMIN','CUSTOMER')")
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> fetchuserbyId(@PathVariable Long id){
		UserResponse userresponse=userservice.getUserById(id);
		return new ResponseEntity<UserResponse>(userresponse,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('THEATRE_ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<List<UserResponse>> fetchallusers(){
		List<UserResponse> listUserRespone = userservice.getAllUsers();
		return new ResponseEntity<List<UserResponse>>(listUserRespone,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('THEATRE_ADMIN')")
	@GetMapping("/status")
	public ResponseEntity<List<UserResponse>> fetchallusersByActiveStatus(){
		List<UserResponse> listUserRespone = userservice.getAllbystatus();
		return new ResponseEntity<List<UserResponse>>(listUserRespone,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('THEATRE_ADMIN','CUSTOMER')")
	@PutMapping("/update/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UpdateUserRequest updateuserreq){
		UserResponse userresp = userservice.updateUser(id, updateuserreq);
		return new ResponseEntity<UserResponse>(userresp,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('THEATRE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		userservice.deleteUserById(id);
	    return ResponseEntity.ok("User deactivated successfully");
	}

}
