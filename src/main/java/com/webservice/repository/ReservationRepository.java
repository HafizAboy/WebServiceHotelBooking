package com.webservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webservice.model.Reservation;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

	Reservation findByReservationCode(String reservationCode);
	
}
