package com.cinebook.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cinebook.entity.Movie;
import com.cinebook.exception.NoChangesFoundException;
import com.cinebook.exception.RegistrationNotDoneException;
import com.cinebook.exception.ResourceNotFoundException;
import com.cinebook.repo.MovieRepository;
import com.cinebook.vo.MovieRequestDTO;
import com.cinebook.vo.MovieResponseDTO;

import jakarta.transaction.Transactional;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepo;
	
	@Override
	@Transactional
	public MovieResponseDTO registerMovie(MovieRequestDTO movieRequestDto) {
	
		if(!movieRepo.existsByTitle(movieRequestDto.getTitle())) {
			Movie movie = new Movie();
			BeanUtils.copyProperties(movieRequestDto, movie);
			movieRepo.save(movie);
			//return movie responsedto
			MovieResponseDTO movieResponseDto = new MovieResponseDTO();
			BeanUtils.copyProperties(movie, movieResponseDto);
			return movieResponseDto;
		}
		else {
			throw new RegistrationNotDoneException("Movie already exists");
		}
	}
	
	@Transactional
	@Override
	public MovieResponseDTO getMovieById(Long movieId) {
		Movie movie = movieRepo.findById(movieId)
							.orElseThrow(()->new ResourceNotFoundException("Movie not found"));
		
		MovieResponseDTO movieResponseDto = new MovieResponseDTO();
		BeanUtils.copyProperties(movie, movieResponseDto);
		return movieResponseDto;
	}
	
	@Transactional
	@Override
	public Page<MovieResponseDTO> getAllMovies(int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size,Sort.by(Direction.ASC,"id"));
		Page<Movie> moviePage = movieRepo.findAll(pageable);
		
		return moviePage.map(movie->{
			MovieResponseDTO movieResponseDto = new MovieResponseDTO();
			BeanUtils.copyProperties(movie, movieResponseDto);
			return movieResponseDto;
		});		
	}
	
	@Transactional
	@Override
	public MovieResponseDTO updateMovieById(Long movieId, MovieRequestDTO movieRequestDto) {
		Movie existingMovie = movieRepo.findById(movieId)
				.orElseThrow(()->new ResourceNotFoundException("Movie not found"));
		
		if(existingMovie.isSameAs(movieRequestDto)) {
			throw new NoChangesFoundException("No changes detected");
		}
		
		BeanUtils.copyProperties(movieRequestDto, existingMovie);
		movieRepo.save(existingMovie);
		
		MovieResponseDTO movieResponseDto = new MovieResponseDTO();
		BeanUtils.copyProperties(existingMovie, movieResponseDto);
		
		return movieResponseDto;		
	}

	@Override
	@Transactional
	public void removeMovieById(Long movieId) {
		Movie movie = movieRepo.findById(movieId)
				.orElseThrow(()->new ResourceNotFoundException("Movie not found"));
	
		movieRepo.delete(movie);
	}

}
