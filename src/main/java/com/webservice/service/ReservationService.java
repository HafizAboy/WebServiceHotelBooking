package com.webservice.service;

import java.util.List;
import java.util.UUID;

import com.webservice.model.Reservation;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface ReservationService {

	Reservation findById(UUID id);
	Reservation findByReservationCode(String reservationCode);
    List<Reservation> findAllReservations();
	Reservation saveReservation(Reservation reservation) throws Exception;
	Reservation updateReservation(Reservation reservation) throws Exception;
	void deleteReservation(Reservation reservation) throws Exception;
}
