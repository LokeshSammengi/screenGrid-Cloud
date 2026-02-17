package com.cinebook.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false,unique = true)
	private String email;
	
	@Column(nullable = false,unique = true)
	private String mobile;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false,length = 30)
	private UserRole role;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;  //For soft deletion
	
	@Column(nullable = false)
	private String password;

	
	//meta data properties
//	@Version
//	private Integer version;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
//	@CreatedBy
//	private String createdBy;
	
//	private String createdOn;//i think i need to pass the user login data spirng security
	


}
