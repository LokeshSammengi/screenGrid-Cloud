package com.cinebook.service;

import org.springframework.data.domain.Page;

import com.cinebook.vo.TheatreRequestDTO;
import com.cinebook.vo.TheatreResponseDTO;
import com.cinebook.vo.TheatreSummaryDTO;

public interface TheatreService {

	public TheatreSummaryDTO registerTheatre(TheatreRequestDTO theatreRequestDto);
	public TheatreResponseDTO getTheatreById(Long theatreid);
	public Page<TheatreResponseDTO> getAllTheatres(int page,int size);
	public TheatreResponseDTO updateTheatreById(Long theatreId,TheatreRequestDTO theatreRequestDto);
	public void removeTheatreById(Long theatreId);
	
	//Recommanded 
	public TheatreResponseDTO getTheatreByName(String name);
}
