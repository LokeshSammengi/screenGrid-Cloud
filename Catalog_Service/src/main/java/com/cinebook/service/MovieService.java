package com.cinebook.service;

import org.springframework.data.domain.Page;

import com.cinebook.vo.MovieRequestDTO;
import com.cinebook.vo.MovieResponseDTO;


public interface MovieService {

	public MovieResponseDTO registerMovie(MovieRequestDTO movieRequestDto);
	public MovieResponseDTO getMovieById(Long movieId);
	public Page<MovieResponseDTO> getAllMovies(int page,int size);
	public MovieResponseDTO updateMovieById(Long movieId,MovieRequestDTO movieRequestDto);
	public void removeMovieById(Long movieId);
	
	
}
