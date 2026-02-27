package com.cinebook.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinebook.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {
	
	List<Show> findByMovieId(Long movieId);
		
    List<Show> findByScreenId(Long screenId);
      
    List<Show> findByScreenIdAndShowDate(Long screenId, LocalDate showDate);


}
