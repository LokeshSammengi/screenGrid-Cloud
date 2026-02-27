package com.cinebook.service;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cinebook.entity.Screen;
import com.cinebook.entity.Theatre;
import com.cinebook.exception.NoChangesFoundException;
import com.cinebook.exception.RegistrationNotDoneException;
import com.cinebook.exception.ResourceNotFoundException;
import com.cinebook.repo.ScreenRepository;
import com.cinebook.repo.TheatreRepository;
import com.cinebook.vo.ScreenRequestDTO;
import com.cinebook.vo.ScreenResponseDTO;
import com.cinebook.vo.ScreenSummaryDTO;

import jakarta.transaction.Transactional;
@Service
public class ScreenServiceImpl implements ScreenService {

	@Autowired
	private ScreenRepository screenRepo;
	
	@Autowired
	private TheatreRepository theatreRepo;
	
	
	@Override
	@Transactional
	public ScreenSummaryDTO registerScreen(ScreenRequestDTO screenRequestDto) {
		
		if(theatreRepo.existsById(screenRequestDto.getTheatreId())) {
			Screen screen = new Screen();
			Theatre theatre = theatreRepo.findById(screenRequestDto.getTheatreId())
										.orElseThrow(()->new RuntimeException("No theatre found with this id : "+screenRequestDto.getTheatreId()));
			BeanUtils.copyProperties(screenRequestDto, screen);
			screen.setTheatre(theatre);
//			screen.setShows(null); -------------------------------------->implementation pending
			screenRepo.save(screen);
			
			//now return screen response
			ScreenSummaryDTO screenSummaryDto = new ScreenSummaryDTO();
			BeanUtils.copyProperties(screen, screenSummaryDto);
			screenSummaryDto.setTheatreId(theatre.getId());
			screenSummaryDto.setTheatreName(theatre.getName());
			
			return screenSummaryDto;
			
			}
		else {
			throw new RegistrationNotDoneException("screen is not registered...");
		}
	}

	@Override
	public ScreenResponseDTO getScreenById(Long screenId) {
		Screen screen = screenRepo.findById(screenId)
							.orElseThrow(()->new ResourceNotFoundException("NO Screen found with this id :"+ screenId));
		
		Theatre theatre = theatreRepo.findById(screen.getTheatre().getId())
							.orElseThrow(()->new ResourceNotFoundException("NO theatre found with this id :"+screen.getTheatre().getId()));
		
		//If exists then screenresponse return 
		ScreenResponseDTO screenResponseDto = new ScreenResponseDTO();
		BeanUtils.copyProperties(screen, screenResponseDto);
		screenResponseDto.setTheatreId(theatre.getId());
		screenResponseDto.setTheatreName(theatre.getName());
		
//		screenResponseDto.setShows(); -------------------------------------->need to implemented pending
		
		return screenResponseDto;
	}

	@Override
	public Page<ScreenResponseDTO> getAllScreens(int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Screen> pageScreen =screenRepo.findAll(pageable);
		return pageScreen.map(screen->{
			//now convert it to screenResponseDto
			ScreenResponseDTO screenResponseDto = new ScreenResponseDTO();
			BeanUtils.copyProperties(screen, screenResponseDto);
			Theatre theatre = theatreRepo.findById(screen.getTheatre().getId())
					.orElseThrow(()->new ResourceNotFoundException("NO theatre found with this id :"+screen.getTheatre().getId()));
			screenResponseDto.setTheatreId(theatre.getId());
			screenResponseDto.setTheatreName(theatre.getName());
//			screenResponseDto.setShows(); -------------------------------------->need to implemented pending
			return screenResponseDto;
		});
	}

	@Override
	@Transactional
	public ScreenResponseDTO updateScreenById(Long screenId, ScreenRequestDTO screenRequestDto) {
		Screen existingScreen = screenRepo.findById(screenId)
				.orElseThrow(()->new ResourceNotFoundException("NO Screen found with this id :"+ screenId));

		if(existingScreen.isSameAs(screenRequestDto)) {
			throw new NoChangesFoundException("No changes detected");
		}
		
		Theatre theatre = theatreRepo.findById(screenRequestDto.getTheatreId())
										.orElseThrow(()->new ResourceNotFoundException("Theatre not found"));
		
		BeanUtils.copyProperties(screenRequestDto, existingScreen);
		existingScreen.setTheatre(theatre);
//		existingScreen.setShows(null);--------------------------------------->implementation pending
		screenRepo.save(existingScreen);
		
		//now return the screen response updated one
		ScreenResponseDTO screenResponseDto = new ScreenResponseDTO();
		BeanUtils.copyProperties(existingScreen, screenResponseDto);
		screenResponseDto.setTheatreId(theatre.getId());
		screenResponseDto.setTheatreName(theatre.getName());
//		screenResponseDto.setShows(null);-------------------------->implementation pending
		return screenResponseDto;
	}

	@Override
	public void removeScreenById(Long screenId) {
		Screen screen = screenRepo.findById(screenId)
				.orElseThrow(()->new ResourceNotFoundException("NO Screen found with this id :"+ screenId));
		
		screenRepo.delete(screen);
	}

}
