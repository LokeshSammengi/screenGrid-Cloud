package com.cinebook.service;

import org.springframework.data.domain.Page;

import com.cinebook.vo.ScreenRequestDTO;
import com.cinebook.vo.ScreenResponseDTO;
import com.cinebook.vo.ScreenSummaryDTO;


public interface ScreenService {

	public ScreenSummaryDTO registerScreen(ScreenRequestDTO screenRequestDto);
	public ScreenResponseDTO getScreenById(Long screenId);
	public Page<ScreenResponseDTO> getAllScreens(int page,int size);
	public ScreenResponseDTO updateScreenById(Long screenId,ScreenRequestDTO screenRequestDto);
	public void removeScreenById(Long screenId);
	
	//Recommanded 
//	public ScreenResponseDTO getScreenByName(String name);
}
