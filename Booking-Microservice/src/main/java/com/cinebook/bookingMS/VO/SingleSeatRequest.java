package com.cinebook.bookingMS.VO;

import com.cinebook.bookingMS.enums.SeatType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SingleSeatRequest {

    @NotNull(message = "screenId is required")
    private Long screenId;

    @NotBlank(message = "seat number is required")
    @Pattern(regexp = "^[A-Z]\\d+$",
             message = "Seat number must be like A1, B12")
    private String seatNumber;

    @NotNull(message = "seatType is required")
    private SeatType seatType;
}
