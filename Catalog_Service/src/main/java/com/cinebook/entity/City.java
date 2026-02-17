package com.cinebook.entity;

import java.util.List;
import java.util.Objects;

import com.cinebook.vo.CityRequestDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;


@Entity
@Data
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "city-theatre")
	private List<Theatre> theatre;
	
	
	public boolean isSameAs(CityRequestDTO otherUpdateCity) {
		return Objects.equals(this.name, otherUpdateCity.getName());
	}

}
