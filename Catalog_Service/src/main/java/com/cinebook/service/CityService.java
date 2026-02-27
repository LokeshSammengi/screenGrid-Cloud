package com.cinebook.service;

import org.springframework.data.domain.Page;

import com.cinebook.vo.CityRequestDTO;
import com.cinebook.vo.CityResponseDTO;
import com.cinebook.vo.CitySummaryDTO;

public interface CityService {
	
	public CitySummaryDTO registerCity(CityRequestDTO cityRequestDto);
	public CityResponseDTO getCityById(Long cityid);
	public Page<CityResponseDTO> getAllCities(int page,int size);
	public String updateCityById(Long cityId,CityRequestDTO cityRequestDto);
	public String removeCityById(Long cityId);
	
	//Recommanded 
	public CityResponseDTO getCityByName(String name);
	

}
