package com.cinebook.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cinebook.entity.Movie;
import com.cinebook.entity.Screen;
import com.cinebook.entity.Show;
import com.cinebook.exception.NoChangesFoundException;
import com.cinebook.exception.RegistrationNotDoneException;
import com.cinebook.exception.ResourceNotFoundException;
import com.cinebook.repo.MovieRepository;
import com.cinebook.repo.ScreenRepository;
import com.cinebook.repo.ShowRepository;
import com.cinebook.vo.ShowRequestDTO;
import com.cinebook.vo.ShowResponseDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShowServiceImpl implements ShowService {

	@Autowired
	private ShowRepository showRepo;

	@Autowired
	private ScreenRepository screenRepo;

	@Autowired
	private MovieRepository movieRepo;

	@Override
	public ShowResponseDTO registerShow(ShowRequestDTO showRequestDto) {

		Movie movie = movieRepo.findById(showRequestDto.getMovieId())
				.orElseThrow(() -> new ResourceNotFoundException("Movie Not found"));
		Screen screen = screenRepo.findById(showRequestDto.getScreenId())
				.orElseThrow(() -> new ResourceNotFoundException("Screen Not found"));

		validateShowOverlap(
		        screen,
		        movie,
		        showRequestDto.getShowDate(),
		        showRequestDto.getShowTime()
		);

		Show show = new Show();
		BeanUtils.copyProperties(showRequestDto, show);
		show.setMovie(movie);
		show.setScreen(screen);

		showRepo.save(show);

		// return the response object
		ShowResponseDTO showResponseDto = new ShowResponseDTO();
		BeanUtils.copyProperties(show, showResponseDto);
		showResponseDto.setMovieId(show.getMovie().getId());
		showResponseDto.setMovieTitle(show.getMovie().getTitle());
		showResponseDto.setScreenId(show.getScreen().getId());
		showResponseDto.setScreenName(show.getScreen().getName());
		return showResponseDto;

	}

	@Override
	public ShowResponseDTO getShowById(Long showId) {
		Show show = showRepo.findById(showId).orElseThrow(() -> new ResourceNotFoundException("Show Not found"));

		ShowResponseDTO showResponseDto = new ShowResponseDTO();
		BeanUtils.copyProperties(show, showResponseDto);
		showResponseDto.setMovieId(show.getMovie().getId());
		showResponseDto.setMovieTitle(show.getMovie().getTitle());
		showResponseDto.setScreenId(show.getScreen().getId());
		showResponseDto.setScreenName(show.getScreen().getName());
		showResponseDto.setShowDate(show.getShowDate());
		showResponseDto.setShowTime(show.getShowTime());
		return showResponseDto;
	}

	@Override
	public Page<ShowResponseDTO> getAllShows(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "id"));
		Page<Show> showPage = showRepo.findAll(pageable);
		return showPage.map(show -> {
			ShowResponseDTO showResponseDto = new ShowResponseDTO();
			BeanUtils.copyProperties(show, showResponseDto);
			showResponseDto.setMovieId(show.getMovie().getId());
			showResponseDto.setMovieTitle(show.getMovie().getTitle());
			showResponseDto.setScreenId(show.getScreen().getId());
			showResponseDto.setScreenName(show.getScreen().getName());
			showResponseDto.setShowDate(show.getShowDate());
			showResponseDto.setShowTime(show.getShowTime());
			return showResponseDto;
		});

	}

	@Override
	public ShowResponseDTO updateShowById(Long showId, ShowRequestDTO showRequestDto) {
		Show existingshow = showRepo.findById(showId)
				.orElseThrow(() -> new ResourceNotFoundException("Show Not found"));

		if (existingshow.isSameAs(showRequestDto)) {
			throw new NoChangesFoundException("No changes detected");
		}
		Movie movie = movieRepo.findById(showRequestDto.getMovieId())
				.orElseThrow(() -> new ResourceNotFoundException("Movie Not found"));
		Screen screen = screenRepo.findById(showRequestDto.getScreenId())
				.orElseThrow(() -> new ResourceNotFoundException("Movie Not found"));


		validateShowOverlap(
		        screen,
		        movie,
		        showRequestDto.getShowDate(),
		        showRequestDto.getShowTime(),
		        showId
		);
		
		BeanUtils.copyProperties(showRequestDto, existingshow);
		existingshow.setMovie(movie);
		existingshow.setScreen(screen);

		showRepo.save(existingshow);

		// return the response object
		ShowResponseDTO showResponseDto = new ShowResponseDTO();
		BeanUtils.copyProperties(existingshow, showResponseDto);
		showResponseDto.setMovieId(existingshow.getMovie().getId());
		showResponseDto.setMovieTitle(existingshow.getMovie().getTitle());
		showResponseDto.setScreenId(existingshow.getScreen().getId());
		showResponseDto.setScreenName(existingshow.getScreen().getName());
		showResponseDto.setShowDate(existingshow.getShowDate());
		showResponseDto.setShowTime(existingshow.getShowTime());
		return showResponseDto;
	}

	@Override
	public void removeShowById(Long showId) {
		Show show = showRepo.findById(showId).orElseThrow(() -> new ResourceNotFoundException("Show Not found"));

		showRepo.delete(show);
	}

	private void validateShowOverlap(Screen screen, Movie movie, LocalDate date, LocalTime time) {

		List<Show> existingShows = showRepo.findByScreenIdAndShowDate(screen.getId(), date);

		LocalTime newStart = time;
		LocalTime newEnd = time.plusMinutes(movie.getDuration());

		for (Show existing : existingShows) {

			LocalTime existingStart = existing.getShowTime();
			LocalTime existingEnd = existingStart.plusMinutes(existing.getMovie().getDuration());

			boolean isOverlapping = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

			if (isOverlapping) {
				throw new RegistrationNotDoneException("Show timing overlaps with existing show");
			}
		}
	}
	
	private void validateShowOverlap(Screen screen, Movie movie, LocalDate date, LocalTime time, Long currentShowId) {

		List<Show> existingShows = showRepo.findByScreenIdAndShowDate(screen.getId(), date);

		LocalTime newStart = time;
		LocalTime newEnd = time.plusMinutes(movie.getDuration());

		for (Show existing : existingShows) {

			if (currentShowId != null && existing.getId().equals(currentShowId)) {
				continue;
			}

			LocalTime existingStart = existing.getShowTime();
			LocalTime existingEnd = existingStart.plusMinutes(existing.getMovie().getDuration());

			boolean isOverlapping = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

			if (isOverlapping) {
				throw new RegistrationNotDoneException("Show timing overlaps with existing show");
			}
		}
	}

}
