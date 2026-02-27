package com.cinebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinebook.service.MovieService;
import com.cinebook.vo.MovieRequestDTO;
import com.cinebook.vo.MovieResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/catalog")
public class MovieController {

	@Autowired
	private MovieService movieservice;
	
	@PostMapping("/movie")
	public ResponseEntity<MovieResponseDTO> createMovie(@Valid @RequestBody MovieRequestDTO movierequestdto){
		MovieResponseDTO movieResponseDto=movieservice.registerMovie(movierequestdto);
		return new ResponseEntity<MovieResponseDTO>(movieResponseDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/movie/{movieId}")
	public ResponseEntity<MovieResponseDTO> fetchTheatreById(@PathVariable Long movieId){
		MovieResponseDTO movieResponseDto=movieservice.getMovieById(movieId);
		return new ResponseEntity<MovieResponseDTO>(movieResponseDto,HttpStatus.OK);
	}
	
//	GET /catalog/cities?page=0&size=3
	@GetMapping("/movie")
	public ResponseEntity< Page<MovieResponseDTO>> fetchAllMovies(@RequestParam(defaultValue = "0") int page,
																	@RequestParam(defaultValue = "5") int size){
		 Page<MovieResponseDTO> movie = movieservice.getAllMovies(page,size);
		return new ResponseEntity<Page<MovieResponseDTO>>(movie,HttpStatus.OK);
	}
	
	@PutMapping("/movie/{movieId}")	
	public ResponseEntity<MovieResponseDTO> upgradeMovieById(@PathVariable Long movieId,
													@Valid @RequestBody MovieRequestDTO movieRequestDto){
		MovieResponseDTO movieResponseDto=movieservice.updateMovieById(movieId, movieRequestDto);
		return new ResponseEntity<MovieResponseDTO>(movieResponseDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/movie/{movieId}")
	public ResponseEntity<String> deleteMovieById(@PathVariable Long movieId){
		movieservice.removeMovieById(movieId);
		return ResponseEntity.noContent().build();
	}
	
	
}
