package com.cinebook.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.cinebook.vo.ShowRequestDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shows")
@Getter
@Setter
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	@JsonBackReference(value = "movie-show")
	private Movie movie;

	
	@ManyToOne
	@JoinColumn(name = "screen_id", nullable = false)
	@JsonBackReference(value = "screen-show")
	private Screen screen;

	 	
	private LocalTime showTime;
	private LocalDate showDate;
	private Double price;
	
	 public boolean isSameAs(ShowRequestDTO other) {
	    	return Objects.equals(this.movie.getId(), other.getMovieId())&&
	    			Objects.equals(this.screen.getId(), other.getScreenId())&&
	    			Objects.equals(this.showTime, other.getShowTime())&&
	    			Objects.equals(this.showDate, other.getShowDate())&&
	    			Objects.equals(this.price, other.getPrice());
	    }
	
}
