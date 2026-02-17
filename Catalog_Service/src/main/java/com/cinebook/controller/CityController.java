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

import com.cinebook.service.CityService;
import com.cinebook.vo.CityRequestDTO;
import com.cinebook.vo.CityResponseDTO;
import com.cinebook.vo.CitySummaryDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/catalog")
public class CityController {

	@Autowired
	private CityService cityservice;
	
	@PostMapping("/cities")
	public ResponseEntity<CitySummaryDTO> createCity(@Valid @RequestBody CityRequestDTO cityrequestdto){
		CitySummaryDTO citySummaryDto=cityservice.registerCity(cityrequestdto);
		return new ResponseEntity<CitySummaryDTO>(citySummaryDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/cities/{cityid}")
	public ResponseEntity<CityResponseDTO> fetchCityById(@PathVariable Long cityid){
		CityResponseDTO cityResponseDto=cityservice.getCityById(cityid);
		return new ResponseEntity<CityResponseDTO>(cityResponseDto,HttpStatus.OK);
	}
	
//	GET /catalog/cities?page=0&size=3
	@GetMapping("/cities")
	public ResponseEntity< Page<CityResponseDTO>> fetchAllCities(@RequestParam(defaultValue = "0") int page,
																	@RequestParam(defaultValue = "5") int size){
		 Page<CityResponseDTO> cities = cityservice.getAllCities(page,size);
		return new ResponseEntity<Page<CityResponseDTO>>(cities,HttpStatus.OK);
	}
	
	@PutMapping("/cities/{cityId}")	
	public ResponseEntity<String> upgradeCityById(@PathVariable Long cityId,
													@Valid @RequestBody CityRequestDTO cityRequestDto){
		String msg=cityservice.updateCityById(cityId, cityRequestDto);
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	@DeleteMapping("/cities/{cityId}")
	public ResponseEntity<String> deleteCityById(@PathVariable Long cityId){
		String msg = cityservice.removeCityById(cityId);
		return new ResponseEntity<String>(msg,HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/cities/name/{cityName}")
	public ResponseEntity<CityResponseDTO> fetchCityByName(@PathVariable String cityName){
		CityResponseDTO cityResponseDto = cityservice.getCityByName(cityName);
		return new ResponseEntity<CityResponseDTO>(cityResponseDto,HttpStatus.OK);
	}
	
}
