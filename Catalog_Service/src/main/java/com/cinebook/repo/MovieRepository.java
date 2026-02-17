package com.cinebook.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinebook.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	boolean existsByTitle(String title);
}
