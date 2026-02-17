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

import com.cinebook.service.ShowService;
import com.cinebook.vo.ShowRequestDTO;
import com.cinebook.vo.ShowResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/catalog")
public class ShowController {

	@Autowired
	private ShowService showservice;
	
	@PostMapping("/show")
	public ResponseEntity<ShowResponseDTO> createShow(@Valid @RequestBody ShowRequestDTO showrequestdto){
		ShowResponseDTO showResponseDto=showservice.registerShow(showrequestdto);
		return new ResponseEntity<ShowResponseDTO>(showResponseDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/show/{showId}")
	public ResponseEntity<ShowResponseDTO> fetchShowById(@PathVariable Long showId){
		ShowResponseDTO showResponseDto=showservice.getShowById(showId);
		return new ResponseEntity<ShowResponseDTO>(showResponseDto,HttpStatus.OK);
	}
	
//	GET /catalog/cities?page=0&size=3
	@GetMapping("/show")
	public ResponseEntity< Page<ShowResponseDTO>> fetchAllShows(@RequestParam(defaultValue = "0") int page,
																	@RequestParam(defaultValue = "5") int size){
		 Page<ShowResponseDTO> shows = showservice.getAllShows(page,size);
		return new ResponseEntity<Page<ShowResponseDTO>>(shows,HttpStatus.OK);
	}
	
	@PutMapping("/show/{showId}")	
	public ResponseEntity<ShowResponseDTO> upgradeShowById(@PathVariable Long showId,
													@Valid @RequestBody ShowRequestDTO showRequestDto){
		ShowResponseDTO showResponseDto=showservice.updateShowById(showId, showRequestDto);
		return new ResponseEntity<ShowResponseDTO>(showResponseDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/show/{showId}")
	public ResponseEntity<String> deleteShowById(@PathVariable Long showId){
		showservice.removeShowById(showId);
		return ResponseEntity.noContent().build();
	}
	
	
}

