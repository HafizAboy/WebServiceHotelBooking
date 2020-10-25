package com.webservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservice.model.Reservation;
import com.webservice.repository.ReservationRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
	private ReservationRepository reservationRepo;

	@Override
	public Reservation findById(UUID id) {
		// TODO Auto-generated method stub
		return reservationRepo.getOne(id);
	}
	
	@Override
	public List<Reservation> findAllReservations() {
		// TODO Auto-generated method stub
		return (List<Reservation>)reservationRepo.findAll();
	}

	@Override
	public Reservation saveReservation(Reservation reservation) throws Exception {
		// TODO Auto-generated method stub
		return reservationRepo.save(reservation);
	}

	@Override
	public Reservation updateReservation(Reservation reservation) throws Exception {
		// TODO Auto-generated method stub
		return saveReservation(reservation);
	}

	@Override
	public void deleteReservation(Reservation reservation) throws Exception {
		// TODO Auto-generated method stub
		reservationRepo.delete(reservation);
	}

	@Override
	public Reservation findByReservationCode(String reservationCode) {
		// TODO Auto-generated method stub
		return reservationRepo.findByReservationCode(reservationCode);
	}

}
