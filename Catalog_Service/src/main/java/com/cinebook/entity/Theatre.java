package com.cinebook.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Theatre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false,updatable = true)
	private String name;
	
	@Column(nullable = false, updatable = true)
	private String location;
	
	//foregin key
	@ManyToOne
	@JoinColumn(name = "city_id", nullable = false)
	@JsonBackReference(value = "city-theatre")
	private City city;

	
	@OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "theatre-screen")
	private List<Screen> screen;


}
