package com.cinebook.payment.VO;


import com.cinebook.payment.enums.SeatStatus;

import lombok.Data;

@Data

public class ShowSeatResponseDto {

    private Long showId;
    private Long seatId;
    private Double price;
    private SeatStatus seatStatus;
}
