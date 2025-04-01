package com.ezen.matzip.domain.reservation.repository;

import com.ezen.matzip.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r, r.restaurantCode FROM Reservation r where r.userCode = :userCode")
    List<Object[]> findReservationByUserCode(int userCode);
}
