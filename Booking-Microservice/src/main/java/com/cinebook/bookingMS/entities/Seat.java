package com.cinebook.bookingMS.entities;



import org.springframework.web.bind.annotation.RequestMapping;

import com.cinebook.bookingMS.enums.SeatType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="seats",
		uniqueConstraints = @UniqueConstraint(columnNames = { "screen_id","seat_number" }))

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "screen_id",nullable = false)
    @NonNull
    private Long screenId; // from Catalog service

    @Column(name = "seat_number",nullable = false)
    @NonNull
    private String seatNumber; // A1, A2

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable=false)
    private SeatType seatType; //REGULAR,PREMIUM,RECLINER
	
}
