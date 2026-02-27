package com.cinebook.entity;


import java.util.List;
import java.util.Objects;

import com.cinebook.vo.MovieRequestDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	private String language;
	
	@Column(nullable = false)
	private Integer duration; //minutes
	
	@Column(nullable = false)
	private String genre;  //like romatic,drama,thriller etc
	
	@OneToMany(mappedBy = "movie")
	@JsonManagedReference(value = "movie-show")
	private List<Show> show;
	
	public boolean isSameAs(MovieRequestDTO movieRequestdto) {
		return Objects.equals(this.title, movieRequestdto.getTitle())&&
				Objects.equals(this.language, movieRequestdto.getLanguage())&&
				Objects.equals(this.duration, movieRequestdto.getDuration())&&
				Objects.equals(this.genre, movieRequestdto.getGenre());
	}


}
