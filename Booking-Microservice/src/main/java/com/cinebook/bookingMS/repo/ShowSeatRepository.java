package com.cinebook.bookingMS.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinebook.bookingMS.entities.ShowSeat;
import com.cinebook.bookingMS.enums.SeatStatus;

import jakarta.persistence.LockModeType;

public interface ShowSeatRepository extends JpaRepository<ShowSeat,Long>{

	boolean existsByShowId(Long showId);
	
	List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findByShowIdAndSeatStatus(Long showId, SeatStatus seatStatus);

    Optional<ShowSeat> findByShowIdAndSeatId(Long showId, Long seatId);

	List<ShowSeat> findByShowIdAndSeatIdIn(Long showId, List<Long> seatIds);

	List<ShowSeat> findBySeatStatusAndLockedAtBefore(SeatStatus locked, LocalDateTime expiryTime);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM ShowSeat s WHERE s.showId = :showId AND s.seatId IN :seatIds")
	List<ShowSeat> findByShowIdAndSeatIdInForUpdate(
	        @Param("showId") Long showId,
	        @Param("seatIds") List<Long> seatIds
	);
    
}
