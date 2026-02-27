package com.cinebook.bookingMS.entities;


import java.time.LocalDateTime;

import com.cinebook.bookingMS.enums.SeatStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "show_seats",
		uniqueConstraints = @UniqueConstraint(columnNames = { "show_id","seat_id" }))
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "show_id",nullable = false)
    @NonNull
    private Long showId;

    @Column(name = "seat_id",nullable = false)
    @NonNull
    private Long seatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status")
    @NonNull
    private SeatStatus seatStatus; //Available/Locked/Booked

    @Column(nullable = false)
    @NonNull
    private Double price;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Version   // 🔥 Optimistic Locking
    private Integer version;
    
    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference(value ="booking-showseats")
    private Booking booking;

}

