package com.cinebook.bookingMS.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cinebook.bookingMS.VO.ScreenResponseDTO;

@FeignClient(name = "Catalog-Service")
public interface CatalogClient {

	@GetMapping("/catalog/screen/{screenId}")
	public ScreenResponseDTO fetchScreenById(@PathVariable Long screenId);
	
}
