package com.cinebook.entity;

import java.util.List;
import java.util.Objects;

import com.cinebook.vo.ScreenRequestDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer totalSeats;

    @ManyToOne
    @JoinColumn(name = "theatre_id", nullable = false)
    @JsonBackReference(value = "theatre-screen")
    private Theatre theatre;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "screen-show")
    private List<Show> shows;
    
    public boolean isSameAs(ScreenRequestDTO updatedScreen) {
    	return Objects.equals(this.name, updatedScreen.getName())&&
    			Objects.equals(this.totalSeats, updatedScreen.getTotalSeats())&&
    			Objects.equals(this.theatre.getId(), updatedScreen.getTheatreId());
    }

}
