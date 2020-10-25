package com.webservice.api;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.uuid.Generators;
import com.webservice.constants.ErrorEnum;
import com.webservice.exceptions.WebserviceException;
import com.webservice.model.Customer;
import com.webservice.model.Payment;
import com.webservice.model.Reservation;
import com.webservice.service.CustomerService;
import com.webservice.service.PaymentService;
import com.webservice.service.ReservationService;
import com.webservice.view.PaymentView;

import io.swagger.annotations.ApiOperation;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/payments")
public class PaymentController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PaymentService pymtService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ReservationService reservationService;

	/**
	 * Fetch a list of payments
	 * @return a list of payments
	 * @throws Exception 
	 */
	@RequestMapping(path="/allPayments", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all payments")
	public ResponseEntity<?> payments() throws Exception {
		List<Payment> payments = (List<Payment>) pymtService.findAllPayments();

		return new ResponseEntity<>(payments, HttpStatus.OK);
	}

	/**
	 * Add a payment
	 * 
	 * @param payment
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/newPayment",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new payment")
	public ResponseEntity<PaymentView> addPayment(@RequestBody PaymentView paymentView) throws Exception {

		Payment savedPayment = new Payment();
		Customer customer = customerService.findByCustomerCode(paymentView.getCustomerCode());
		Reservation reservation = reservationService.findByReservationCode(paymentView.getReservationCode());

		if(customer == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Customer");
		}else if(reservation == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Reservation");
		}
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PaymentView>> violations = validator.validate(paymentView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<PaymentView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//generate id for code
			UUID uuid = Generators.timeBasedGenerator().generate();
			String transactionCode = uuid.toString();

			//set data to entity
			savedPayment.setTotalAmount(paymentView.getTotalAmount());
			savedPayment.setReservation(reservation);
			savedPayment.setCustomer(customer);
			savedPayment.setTransactionCode(transactionCode);
			
			try {
				logger.info("Saving Payment");
				pymtService.savePayment(savedPayment);
				logger.info("Payment creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create payment");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<PaymentView>(paymentView, HttpStatus.CREATED);
	}

	/**
	 * Updates the payment
	 * 
	 * @param payment
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updatePayment/{transactionCode}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a payment")
	public ResponseEntity<Payment> updatePayment(@PathVariable String transactionCode, @RequestParam String totalAmount) throws Exception {

		Payment savedPayment = new Payment();

		//find Payment by code
		Payment paymentFindByTrxnCode = pymtService.findByPaymentCode(transactionCode);
		//null checking
		if(paymentFindByTrxnCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Payment");
		}
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Payment>> violations = validator.validate(paymentFindByTrxnCode);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<Payment> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");
			
			//update data to entity
			BigDecimal totalAmt=new BigDecimal(totalAmount);
			paymentFindByTrxnCode.setTotalAmount(totalAmt);

			try {
				logger.info("Update Payment");
				savedPayment = pymtService.updatePayment(paymentFindByTrxnCode);
				logger.info("Payment updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update payment");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		
		return new ResponseEntity<Payment>(savedPayment, HttpStatus.OK);
	}

	/**
	 * Deletes payment identified with <code>transactionCode</code>
	 * @param id
	 * @throws Exception 
	 */
	@RequestMapping(path = "/deletePayment/{transactionCode}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a payment")
	public ResponseEntity<?> deletepayments(@PathVariable String transactionCode) throws Exception {

		//find Payment by code
		Payment paymentFindByTrxnCode = pymtService.findByPaymentCode(transactionCode);
		//null checking
		if(paymentFindByTrxnCode == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Payment");
		}
		
		try {
			logger.info("Delete payments");
			pymtService.deletePayment(paymentFindByTrxnCode);
			logger.info("payments Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete payment");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
