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

import com.cinebook.service.ScreenService;
import com.cinebook.vo.ScreenRequestDTO;
import com.cinebook.vo.ScreenResponseDTO;
import com.cinebook.vo.ScreenSummaryDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/catalog")
public class ScreenController {

	@Autowired
	private ScreenService screenservice;
	
	@PostMapping("/screen")
	public ResponseEntity<ScreenSummaryDTO> createScreen(@Valid @RequestBody ScreenRequestDTO screenRequestdto){
		ScreenSummaryDTO screenSummaryDto = screenservice.registerScreen(screenRequestdto);
		return new ResponseEntity<ScreenSummaryDTO>(screenSummaryDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/screen/{screenId}")
	public ResponseEntity<ScreenResponseDTO> fetchScreenById(@PathVariable Long screenId){
		ScreenResponseDTO screenResponseDto=screenservice.getScreenById(screenId);
		return new ResponseEntity<ScreenResponseDTO>(screenResponseDto,HttpStatus.OK);
	}
	
//	GET /catalog/cities?page=0&size=3
	@GetMapping("/screen")
	public ResponseEntity< Page<ScreenResponseDTO>> fetchAllScreens(@RequestParam(defaultValue = "0") int page,
																	@RequestParam(defaultValue = "5") int size){
		 Page<ScreenResponseDTO> screen = screenservice.getAllScreens(page,size);
		return new ResponseEntity<Page<ScreenResponseDTO>>(screen,HttpStatus.OK);
	}
	
	@PutMapping("/screen/{screenId}")	
	public ResponseEntity<ScreenResponseDTO> upgradeScreenById(@PathVariable Long screenId,
													@Valid @RequestBody ScreenRequestDTO screenRequestDto){
		ScreenResponseDTO screenResponseDto=screenservice.updateScreenById(screenId, screenRequestDto);
		return new ResponseEntity<ScreenResponseDTO>(screenResponseDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/screen/{screenId}")
	public ResponseEntity<String> deleteScreenById(@PathVariable Long screenId){
		screenservice.removeScreenById(screenId);
		return ResponseEntity.noContent().build();
	}
}
