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
import com.webservice.service.HotelService;
import com.webservice.view.HotelView;

import io.swagger.annotations.ApiOperation;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/hotels")
public class HotelController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HotelService hotelService;

	/**
	 * Fetch a list of hotels
	 * @return a list of hotels
	 * @throws Exception 
	 */
	@RequestMapping(path="/allHotels", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all hotels")
	public ResponseEntity<?> hotels() throws Exception {
		List<Hotel> hotels = (List<Hotel>) hotelService.findAllHotels();

		return new ResponseEntity<>(hotels, HttpStatus.OK);
	}

	/**
	 * Add a hotel
	 * 
	 * @param hotel
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/newHotel",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new hotel")
	public ResponseEntity<Hotel> addHotel(@RequestBody HotelView hotelView) throws Exception {

		Hotel savedHotel = new Hotel();

		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<HotelView>> violations = validator.validate(hotelView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<HotelView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//generate id for code
			UUID uuid = Generators.timeBasedGenerator().generate();
			String hotelCode = uuid.toString();
			
			//set data to entity
			savedHotel.setHotelName(hotelView.getHotelName());
			savedHotel.setHotelAddress(hotelView.getHotelAddress());
			savedHotel.setHotelCode(hotelCode);
			
			try {
				logger.info("Saving Hotel");
				hotelService.saveHotel(savedHotel);
				logger.info("Hotel creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create hotel");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<Hotel>(savedHotel, HttpStatus.CREATED);
	}

	/**
	 * Updates the hotel
	 * 
	 * @param hotel
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updateHotel/{hotelCode}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a hotel")
	public ResponseEntity<Hotel> updateHotel(@PathVariable String hotelCode, @RequestBody HotelView hotelView) throws Exception {

		Hotel savedHotel = new Hotel();
		
		//find Hotel by code
		Hotel hotelFindByHotelCode = hotelService.findByHotelCode(hotelCode);
		//null checking
		if(hotelFindByHotelCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Hotel");
		}
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<HotelView>> violations = validator.validate(hotelView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<HotelView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//update data to entity
			hotelFindByHotelCode.setHotelName(hotelView.getHotelName());
			hotelFindByHotelCode.setHotelAddress(hotelView.getHotelAddress());

			try {
				logger.info("Update Hotel");
				savedHotel = hotelService.updateHotel(hotelFindByHotelCode);
				logger.info("Hotel updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update hotel");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		
		return new ResponseEntity<Hotel>(savedHotel, HttpStatus.OK);
	}

	/**
	 * Deletes hotel identified with <code>hotelCode</code>
	 * @param id
	 * @throws Exception 
	 */
	@RequestMapping(path = "/deleteHotel/{hotelCode}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a hotel")
	public ResponseEntity<?> deletehotels(@PathVariable String hotelCode) throws Exception {

		//find Hotel by code
		Hotel hotelFindByHotelCode = hotelService.findByHotelCode(hotelCode);
		//null checking
		if(hotelFindByHotelCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Hotel");
		}
		
		try {
			logger.info("Delete hotels");
			hotelService.deleteHotel(hotelFindByHotelCode);
			logger.info("hotels Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete hotel");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
