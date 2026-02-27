package com.cinebook.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinebook.entity.City;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
	
	public Optional<City> findByName(String name);
	
	boolean existsByName(String name);
	
	

}
