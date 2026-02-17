package com.cinebook.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinebook.entity.Theatre;

public interface TheatreRepository extends JpaRepository<Theatre,Long> {
	
	Optional<Theatre> findByName(String name);
	
	boolean existsByNameAndCityId(String name, Long cityId);

}
