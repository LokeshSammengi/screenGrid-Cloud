package com.cinebook.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinebook.entities.User;
import com.cinebook.entities.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	Boolean existsByEmail(String email);
	
	Boolean existsByMobile(String mobilenumber);
	
	List<User> findAllByStatus(UserStatus userstatus);

}
