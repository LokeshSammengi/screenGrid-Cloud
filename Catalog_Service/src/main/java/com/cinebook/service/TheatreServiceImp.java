package com.cinebook.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cinebook.entity.City;
import com.cinebook.entity.Theatre;
import com.cinebook.exception.DuplicateResourceException;
import com.cinebook.exception.ResourceNotFoundException;
import com.cinebook.repo.CityRepository;
import com.cinebook.repo.TheatreRepository;
import com.cinebook.vo.ScreenResponseDTO;
import com.cinebook.vo.ShowResponseDTO;
import com.cinebook.vo.TheatreRequestDTO;
import com.cinebook.vo.TheatreResponseDTO;
import com.cinebook.vo.TheatreSummaryDTO;

import jakarta.transaction.Transactional;

@Service
public class TheatreServiceImp implements TheatreService {

	@Autowired
	private TheatreRepository theatreRepo;

	@Autowired
	private CityRepository cityRepo;

	@Transactional
	public TheatreSummaryDTO registerTheatre(TheatreRequestDTO theatreRequestDto) {

		if (!theatreRepo.existsByNameAndCityId(theatreRequestDto.getName(), theatreRequestDto.getCityId())) {
			Theatre theatre = new Theatre();
			City city = cityRepo.findById(theatreRequestDto.getCityId()).orElseThrow(
					() -> new RuntimeException("City not found with id: " + theatreRequestDto.getCityId()));

			BeanUtils.copyProperties(theatreRequestDto, theatre);
			theatre.setCity(city);
			theatreRepo.save(theatre);

			TheatreSummaryDTO theatreSummaryDto = new TheatreSummaryDTO();
			BeanUtils.copyProperties(theatreRequestDto, theatreSummaryDto);
			theatreSummaryDto.setCityId(city.getId());
			theatreSummaryDto.setCityName(city.getName());

			return theatreSummaryDto;

		} else {
			throw new DuplicateResourceException("Theatre has already existed with name " + theatreRequestDto.getName()
					+ " and with city id :" + theatreRequestDto.getCityId());
		}

	}

	@Override
	@Transactional
	public TheatreResponseDTO getTheatreById(Long theatreid) {
		// check whether theatre exist arenot
		Theatre theatre = theatreRepo.findById(theatreid)
				.orElseThrow(() -> new ResourceNotFoundException("Theatre not found with this id : " + theatreid));

		TheatreResponseDTO theatreResponseDto = new TheatreResponseDTO();
		City city = cityRepo.findById(theatre.getCity().getId()).orElseThrow(
				() -> new ResourceNotFoundException("city is not found with this id : " + theatre.getCity().getId()));
		BeanUtils.copyProperties(theatre, theatreResponseDto);
		theatreResponseDto.setCityId(city.getId());
		theatreResponseDto.setCityName(city.getName());
		List<ScreenResponseDTO> listScreenResponseDto = theatre.getScreen().stream().map(screen -> {
			ScreenResponseDTO s = new ScreenResponseDTO();
			s.setId(screen.getId());
			s.setName(screen.getName());
			s.setShows(screen.getShows().stream().map(shows -> {
				ShowResponseDTO show = new ShowResponseDTO();
				show.setId(shows.getId());
				show.setMovieId(shows.getMovie().getId());
				show.setMovieTitle(show.getMovieTitle());
				show.setPrice(shows.getPrice());
				return show;
			}).toList());
			s.setTotalSeats(screen.getTotalSeats());
			return s;
		}).toList();
		theatreResponseDto.setScreens(listScreenResponseDto);

//		theatreResponseDto.setScreens();
//		--------------------------------------->implementataion needed

		return theatreResponseDto;
	}

	@Override
	@Transactional
	public Page<TheatreResponseDTO> getAllTheatres(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<Theatre> theatrePage = theatreRepo.findAll(pageable);

		return theatrePage.map(theatre -> {
			TheatreResponseDTO dto = new TheatreResponseDTO();
			City city = cityRepo.findById(theatre.getCity().getId()).orElseThrow(() -> new ResourceNotFoundException(
					"city is not found with this id : " + theatre.getCity().getId()));
			dto.setCityId(city.getId());
			dto.setCityName(city.getName());
			BeanUtils.copyProperties(theatre, dto);
			return dto;
		});
	}

	@Override
	@Transactional
	public TheatreResponseDTO updateTheatreById(Long theatreId, TheatreRequestDTO theatreRequestDto) {

		Theatre theatre = theatreRepo.findById(theatreId)
				.orElseThrow(() -> new ResourceNotFoundException("No theatre found with id :" + theatreId));

		if (theatreRepo.existsByNameAndCityId(theatreRequestDto.getName(), theatreRequestDto.getCityId())) {
			throw new DuplicateResourceException(
					"theatre Name (" + theatreRequestDto.getName() + ") exists in the database");
		}

		BeanUtils.copyProperties(theatreRequestDto, theatre);
		theatreRepo.save(theatre);

		TheatreResponseDTO theatreResponseDto = new TheatreResponseDTO();
		City city = cityRepo.findById(theatre.getCity().getId()).orElseThrow(
				() -> new ResourceNotFoundException("city is not found with this id : " + theatre.getCity().getId()));
		theatreResponseDto.setCityId(city.getId());
		theatreResponseDto.setCityName(city.getName());
//	theatreResponseDto.setScreens();
//	************ -> need to implement the screens here if neccessary
		BeanUtils.copyProperties(theatre, theatreResponseDto);

		return theatreResponseDto;
	}

	@Override
	@Transactional
	public void removeTheatreById(Long theatreId) {
		Theatre theatre = theatreRepo.findById(theatreId)
				.orElseThrow(() -> new ResourceNotFoundException("No theatre found with id :" + theatreId));

		theatreRepo.delete(theatre);

//		return theatre.getName()+" theatre has removed from the database..of id : "+theatreId;

	}

	@Override
	@Transactional
	public TheatreResponseDTO getTheatreByName(String name) {

		Theatre theatre = theatreRepo.findByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("No theatre found with name : " + name));
		TheatreResponseDTO theatreResponseDto = new TheatreResponseDTO();
		City city = cityRepo.findById(theatre.getCity().getId()).orElseThrow(
				() -> new ResourceNotFoundException("city is not found with this id : " + theatre.getCity().getId()));

		BeanUtils.copyProperties(theatre, theatreResponseDto);
		theatreResponseDto.setCityId(city.getId());
		theatreResponseDto.setCityName(city.getName());
//		theatreResponseDto.setScreens(); ----------------------------------------------->implementation needed

		return theatreResponseDto;
	}

}
