package com.webservice.api;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.uuid.Generators;
import com.webservice.constants.ErrorEnum;
import com.webservice.exceptions.WebserviceException;
import com.webservice.model.Customer;
import com.webservice.model.Reservation;
import com.webservice.model.Room;
import com.webservice.service.CustomerService;
import com.webservice.service.ReservationService;
import com.webservice.service.RoomService;
import com.webservice.view.ReservationView;

import io.swagger.annotations.ApiOperation;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/reservations")
public class ReservationController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private RoomService roomService;

	/**
	 * Fetch a list of reservations
	 * @return a list of reservations
	 * @throws Exception 
	 */
	@RequestMapping(path="/allReservations", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all reservations")
	public ResponseEntity<?> reservations() throws Exception {
		List<Reservation> reservations = (List<Reservation>) reservationService.findAllReservations();

		return new ResponseEntity<>(reservations, HttpStatus.OK);
	}

	/**
	 * Add a reservation
	 * 
	 * @param reservation
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/newReservation",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new reservation")
	public ResponseEntity<Reservation> addReservation(@RequestBody ReservationView reservationView) throws Exception {

		Reservation savedReservation = new Reservation();
		Customer customer = customerService.findByCustomerCode(reservationView.getCustomerCode());
		Room room = roomService.findByRoomCode(reservationView.getRoomCode());
		
		if(customer == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Customer");
		}else if(room == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Room");
		}
		
		room.setAvailability("N");
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ReservationView>> violations = validator.validate(reservationView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<ReservationView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//generate id for code
			UUID uuid = Generators.timeBasedGenerator().generate();
			String reservationCode = uuid.toString();

			//set data to entity
			savedReservation.setCheckInDate(reservationView.getCheckInDate());
			savedReservation.setCheckOutDate(reservationView.getCheckOutDate());
			savedReservation.setCustomer(customer);
			savedReservation.setRoom(room);
			savedReservation.setReservationCode(reservationCode);
			savedReservation.setCancellation(reservationView.getCancellation());

			try {
				logger.info("Saving Reservation");
				reservationService.saveReservation(savedReservation);
				logger.info("Reservation creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create reservation");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<Reservation>(savedReservation, HttpStatus.CREATED);
	}

	/**
	 * Updates the reservation
	 * 
	 * @param reservation
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updateReservation/{reservationCode}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a reservation")
	public ResponseEntity<Reservation> updateReservation(@PathVariable String reservationCode, @RequestBody ReservationView reservationView) throws Exception {

		Reservation savedReservation = new Reservation();

		//find Reservation by code
		Reservation reservationFindByCode = reservationService.findByReservationCode(reservationCode);

		//find Room by code
		Room room = roomService.findByRoomCode(reservationView.getRoomCode());
		
		//null checking
		if(reservationFindByCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Reservation");
		}else if(room == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Room");
		}
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ReservationView>> violations = validator.validate(reservationView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<ReservationView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//update data to entity
			reservationFindByCode.setCheckInDate(reservationView.getCheckInDate());
			reservationFindByCode.setCheckOutDate(reservationView.getCheckOutDate());
			reservationFindByCode.setRoom(room);
			reservationFindByCode.setCancellation(reservationView.getCancellation());

			try {
				logger.info("Update Reservation");
				savedReservation = reservationService.updateReservation(reservationFindByCode);
				logger.info("Reservation updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update reservation");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		
		return new ResponseEntity<Reservation>(savedReservation, HttpStatus.OK);
	}

	/**
	 * Deletes reservation identified with <code>reservationCode</code>
	 * @param id
	 * @throws Exception 
	 */
	@RequestMapping(path = "/deleteReservation/{reservationCode}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a reservation")
	public ResponseEntity<?> deletereservations(@PathVariable String reservationCode) throws Exception {

		//find Reservation by code
		Reservation reservationFindByCode = reservationService.findByReservationCode(reservationCode);
		//null checking
		if(reservationFindByCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Reservation");
		}
		
		try {
			logger.info("Delete reservations");
			reservationService.deleteReservation(reservationFindByCode);
			logger.info("reservations Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete reservation");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
