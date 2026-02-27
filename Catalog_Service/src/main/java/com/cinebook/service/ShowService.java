package com.cinebook.service;

import org.springframework.data.domain.Page;

import com.cinebook.vo.ShowRequestDTO;
import com.cinebook.vo.ShowResponseDTO;

public interface ShowService {
	
	public ShowResponseDTO registerShow(ShowRequestDTO showRequestDto);
	public ShowResponseDTO getShowById(Long showId);
	public Page<ShowResponseDTO> getAllShows(int page,int size);
	public ShowResponseDTO updateShowById(Long showId,ShowRequestDTO showRequestDto);
	public void removeShowById(Long showId);
	

}
