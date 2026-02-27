package com.cinebook.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinebook.entity.Screen;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

//	public boolean existsByTheatreName(String theatreName);
	
	List<Screen> findByTheatreId(Long id);
}
