package com.cinebook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cinebook.entities.LoginRequest;
import com.cinebook.entities.User;
import com.cinebook.entities.UserStatus;
import com.cinebook.exception.DuplicateResourceException;
import com.cinebook.exception.ResourceNotFoundException;
import com.cinebook.repo.UserRepository;
import com.cinebook.securityconfig.JwtUtil;
import com.cinebook.vo.UpdateUserRequest;
import com.cinebook.vo.UserRequest;
import com.cinebook.vo.UserResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

//	@Autowired
	private final UserRepository userrepo;
	
	private final PasswordEncoder passwordEncoder;

	private final JwtUtil jwtUtil;

	@Override
	@Transactional
	public UserResponse registerUser(UserRequest userrequest) {

		// check if the email id exists or not
		if (userrepo.existsByEmail(userrequest.getEmail())) {
			throw new DuplicateResourceException("Email already exists");
		}

		// check through the mobile number
		if (userrepo.existsByMobile(userrequest.getMobile())) {
			throw new DuplicateResourceException("Mobile number already exists");
		}

		User user = new User();
		BeanUtils.copyProperties(userrequest, user);

		user.setPassword(passwordEncoder.encode(userrequest.getPassword())); 
		user.setStatus(UserStatus.ACTIVE);
		User saveduser = userrepo.save(user);

		UserResponse userresponse = new UserResponse();
		BeanUtils.copyProperties(saveduser, userresponse);
		return userresponse;
	}
	
	@Override
	public UserResponse getUserById(Long id) {
		User userentity = userrepo.findById(id)
				.orElseThrow(() -> new RuntimeException("User Not Found with this ID " + id));

		UserResponse userresponse = new UserResponse();
		BeanUtils.copyProperties(userentity, userresponse);
		return userresponse;
	}

	@Override
	public List<UserResponse> getAllUsers() {
		List<User> listUserEntity = userrepo.findAll();
		List<UserResponse> listUserResponse = new ArrayList<>();

		listUserEntity.forEach(entity -> {
			UserResponse userresponse = new UserResponse();
			BeanUtils.copyProperties(entity, userresponse);
			listUserResponse.add(userresponse);
		});

		return listUserResponse;
	}

	@Override
	public List<UserResponse> getAllbystatus() {
		List<User> ActivelistUserEntity = userrepo.findAllByStatus(UserStatus.ACTIVE);
		List<UserResponse> listUserResponse = new ArrayList<>();

		ActivelistUserEntity.forEach(entity -> {
			UserResponse userresponse = new UserResponse();
			BeanUtils.copyProperties(entity, userresponse);
			listUserResponse.add(userresponse);
		});

		return listUserResponse;
	}

	@Override
	public UserResponse updateUser(Long id, UpdateUserRequest updateuserrequest) {
		User user = userrepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
		
		user.setName(updateuserrequest.getName());
		user.setEmail(updateuserrequest.getEmail());
		user.setMobile(updateuserrequest.getMobile());
		
		userrepo.save(user);
		
		UserResponse userresponse = new UserResponse();
		BeanUtils.copyProperties(user,userresponse);;
		return userresponse;
	}
	
	
	@Override
	public void deleteUserById(Long id) {
	User user = userrepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
	user.setStatus(UserStatus.INACTIVE);
	userrepo.save(user);
	}
	
	
	public String login(LoginRequest request) {

	    User user = userrepo.findByEmail(request.getEmail())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Invalid credentials");
	    }

	    return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
	}

			
}
