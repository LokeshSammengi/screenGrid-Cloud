package com.cinebook.bookingMS.VO;


import com.cinebook.bookingMS.enums.SeatStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ShowSeatResponseDTO {

    private Long showId;
    private Long seatId;
//    private String seatNumber;   // optional (if you fetch from Seat entity)
    private Double price;
    private SeatStatus seatStatus;
}

//public record ShowSeatResponseDTO(
//        Long seatId,
//        SeatStatus seatStatus,
//        Double price
//) {}
