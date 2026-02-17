package com.cinebook.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cinebook.entity.City;
import com.cinebook.exception.NoChangesFoundException;
import com.cinebook.exception.ResourceNotFoundException;
import com.cinebook.repo.CityRepository;
import com.cinebook.vo.CityRequestDTO;
import com.cinebook.vo.CityResponseDTO;
import com.cinebook.vo.CitySummaryDTO;
import com.cinebook.vo.TheatreInCityDTO;

import jakarta.transaction.Transactional;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
	private CityRepository cityRepo;

	@Override
	@Transactional
	public CitySummaryDTO registerCity(CityRequestDTO cityRequestDto) {
		City city = new City();
		if (!cityRepo.existsByName(cityRequestDto.getName())) {
			BeanUtils.copyProperties(cityRequestDto, city);
			cityRepo.save(city);

			CitySummaryDTO citySummaryDto = new CitySummaryDTO();
			BeanUtils.copyProperties(cityRequestDto, citySummaryDto);
			return citySummaryDto;
		} else {
			throw new ResourceNotFoundException("No city response avaliable...");
		}

	}

	@Override
	@Transactional
	public CityResponseDTO getCityById(Long cityid) {
		City city = cityRepo.findById(cityid)
				.orElseThrow(() -> new ResourceNotFoundException("No city found with id :" + cityid));
		// now convert city to cityresponsedto
		CityResponseDTO cityResponseDto = new CityResponseDTO();
		BeanUtils.copyProperties(city, cityResponseDto);
		List<TheatreInCityDTO> listTheatreSummaryDto = city.getTheatre().stream().map(theatre -> {
			TheatreInCityDTO t = new TheatreInCityDTO();
			t.setId(theatre.getId());
			t.setName(theatre.getName());
			t.setLocation(theatre.getLocation());
			return t;
		}).toList();
		cityResponseDto.setTheatres(listTheatreSummaryDto);

		return cityResponseDto;
	}

	public Page<CityResponseDTO> getAllCities(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<City> cityPage = cityRepo.findAll(pageable);

		return cityPage.map(city -> {
			CityResponseDTO dto = new CityResponseDTO();
			BeanUtils.copyProperties(city, dto);
			List<TheatreInCityDTO> listTheatreSummaryDto = city.getTheatre().stream().map(theatre -> {
				TheatreInCityDTO t = new TheatreInCityDTO();
				t.setId(theatre.getId());
				t.setName(theatre.getName());
				t.setLocation(theatre.getLocation());
				return t;
			}).toList();
			dto.setTheatres(listTheatreSummaryDto);
			return dto;
		});

	}

	@Override
	@Transactional
	public String updateCityById(Long cityId, CityRequestDTO cityRequestDto) {
		City existingCity = cityRepo.findById(cityId)
				.orElseThrow(() -> new ResourceNotFoundException("No city found with id :" + cityId));

		if (existingCity.isSameAs(cityRequestDto)) {
			throw new NoChangesFoundException("No change detected");
		}

		BeanUtils.copyProperties(cityRequestDto, existingCity);
		cityRepo.save(existingCity);

		return "city has successfully updated of id : " + cityId;
	}

	@Override
	@Transactional
	public String removeCityById(Long cityId) {
		City city = cityRepo.findById(cityId)
				.orElseThrow(() -> new ResourceNotFoundException("No city found with id :" + cityId));

		cityRepo.delete(city);

		return city.getName() + " city has removed from the database..of id : " + cityId;
	}

	@Override
	@Transactional
	public CityResponseDTO getCityByName(String name) {

		City city = cityRepo.findByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("No city found with name : " + name));
		CityResponseDTO cityResponseDto = new CityResponseDTO();
		BeanUtils.copyProperties(city, cityResponseDto);
		List<TheatreInCityDTO> listTheatreSummaryDto = city.getTheatre().stream().map(theatre -> {
			TheatreInCityDTO t = new TheatreInCityDTO();
			t.setId(theatre.getId());
			t.setName(theatre.getName());
			t.setLocation(theatre.getLocation());
			return t;
		}).toList();
		cityResponseDto.setTheatres(listTheatreSummaryDto);

		return cityResponseDto;
	}

}
