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

import com.cinebook.service.TheatreService;
import com.cinebook.vo.TheatreRequestDTO;
import com.cinebook.vo.TheatreResponseDTO;
import com.cinebook.vo.TheatreSummaryDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/catalog")
public class TheatreController {

	@Autowired
	private TheatreService theatreservice;
	
	@PostMapping("/theatre")
	public ResponseEntity<TheatreSummaryDTO> createTheatre(@Valid @RequestBody TheatreRequestDTO theatrerequestdto){
		TheatreSummaryDTO theatreSummaryDto=theatreservice.registerTheatre(theatrerequestdto);
		return new ResponseEntity<TheatreSummaryDTO>(theatreSummaryDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/theatre/{theatreId}")
	public ResponseEntity<TheatreResponseDTO> fetchTheatreById(@PathVariable Long theatreId){
		TheatreResponseDTO theatreResponseDto=theatreservice.getTheatreById(theatreId);
		return new ResponseEntity<TheatreResponseDTO>(theatreResponseDto,HttpStatus.OK);
	}
	
//	GET /catalog/cities?page=0&size=3
	@GetMapping("/theatre")
	public ResponseEntity< Page<TheatreResponseDTO>> fetchAllTheatres(@RequestParam(defaultValue = "0") int page,
																	@RequestParam(defaultValue = "5") int size){
		 Page<TheatreResponseDTO> theatre = theatreservice.getAllTheatres(page,size);
		return new ResponseEntity<Page<TheatreResponseDTO>>(theatre,HttpStatus.OK);
	}
	
	@PutMapping("/theatre/{theatreId}")	
	public ResponseEntity<TheatreResponseDTO> upgradeTheatreById(@PathVariable Long theatreId,
													@Valid @RequestBody TheatreRequestDTO theatreRequestDto){
		TheatreResponseDTO theatreResponseDto=theatreservice.updateTheatreById(theatreId, theatreRequestDto);
		return new ResponseEntity<TheatreResponseDTO>(theatreResponseDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/theatre/{theatreId}")
	public ResponseEntity<String> deleteTheatreById(@PathVariable Long theatreId){
		theatreservice.removeTheatreById(theatreId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/theatre/name/{theatreName}")
	public ResponseEntity<TheatreResponseDTO> fetchTheatreByName(@PathVariable String theatreName){
		TheatreResponseDTO theatreResponseDto = theatreservice.getTheatreByName(theatreName);
		return new ResponseEntity<TheatreResponseDTO>(theatreResponseDto,HttpStatus.OK);
	}
	
}
