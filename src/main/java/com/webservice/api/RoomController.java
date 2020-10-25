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
import com.webservice.model.Hotel;
import com.webservice.model.Room;
import com.webservice.service.HotelService;
import com.webservice.service.RoomService;
import com.webservice.view.RoomView;

import io.swagger.annotations.ApiOperation;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/rooms")
public class RoomController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoomService roomService;

	@Autowired
	private HotelService hotelService;

	/**
	 * Fetch a list of rooms
	 * @return a list of rooms
	 * @throws Exception 
	 */
	@RequestMapping(path="/allRooms", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all rooms")
	public ResponseEntity<?> rooms() throws Exception {
		List<Room> rooms = (List<Room>) roomService.findAllRooms();

		return new ResponseEntity<>(rooms, HttpStatus.OK);
	}

	/**
	 * Add a room
	 * 
	 * @param room
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/newRoom",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new room")
	public ResponseEntity<Room> addRoom(@RequestBody RoomView roomView) throws Exception {

		Room savedRoom = new Room();
		Hotel hotel = hotelService.findByHotelCode(roomView.getHotelCode());
		
		if(hotel == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Hotel");
		}
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<RoomView>> violations = validator.validate(roomView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<RoomView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//generate id for code
			UUID uuid = Generators.timeBasedGenerator().generate();
			String roomCode = uuid.toString();

			//set data to entity
			savedRoom.setRoomName(roomView.getRoomName());
			savedRoom.setCapacity(roomView.getCapacity());
			savedRoom.setAvailability(roomView.getAvailability());
			savedRoom.setHotel(hotel);
			savedRoom.setRoomCode(roomCode);
			
			try {
				logger.info("Saving Room");
				roomService.saveRoom(savedRoom);
				logger.info("Room creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create room");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<Room>(savedRoom, HttpStatus.CREATED);
	}

	/**
	 * Updates the room
	 * 
	 * @param room
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updateRoom/{roomCode}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a room")
	public ResponseEntity<Room> updateRoom(@PathVariable String roomCode, @RequestBody RoomView roomView) throws Exception {

		Room savedRoom = new Room();

		//find Room by code
		Room roomFindByRoomCode = roomService.findByRoomCode(roomCode);

		//find Hotel by code
		Hotel hotel = hotelService.findByHotelCode(roomView.getHotelCode());

		//null checking
		if(hotel == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Hotel");
		}else if(roomFindByRoomCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Room");
		}
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<RoomView>> violations = validator.validate(roomView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<RoomView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//update data to entity
			roomFindByRoomCode.setRoomName(roomView.getRoomName());
			roomFindByRoomCode.setCapacity(roomView.getCapacity());
			roomFindByRoomCode.setAvailability(roomView.getAvailability());
			roomFindByRoomCode.setHotel(hotel);

			try {
				logger.info("Update Room");
				savedRoom = roomService.updateRoom(roomFindByRoomCode);
				logger.info("Room updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update room");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		
		return new ResponseEntity<Room>(savedRoom, HttpStatus.OK);
	}

	/**
	 * Deletes room identified with <code>roomCode</code>
	 * @param id
	 * @throws Exception 
	 */
	@RequestMapping(path = "/deleteRoom/{roomCode}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a room")
	public ResponseEntity<?> deleterooms(@PathVariable String roomCode) throws Exception {

		//find Room by code
		Room roomFindByRoomCode = roomService.findByRoomCode(roomCode);
		//null checking
		if(roomFindByRoomCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Room");
		}
		
		try {
			logger.info("Delete room");
			roomService.deleteRoom(roomFindByRoomCode);
			logger.info("rooms Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete room");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
