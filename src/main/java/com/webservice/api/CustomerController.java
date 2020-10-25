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
import com.webservice.service.CustomerService;
import com.webservice.view.CustomerView;

import io.swagger.annotations.ApiOperation;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/customers")
public class CustomerController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerService custService;

	/**
	 * Fetch a list of customers
	 * @return a list of customers
	 * @throws Exception 
	 */
	@RequestMapping(path="/allCustomers", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all customers")
	public ResponseEntity<?> customers() throws Exception {
		List<Customer> customers = (List<Customer>) custService.findAllCustomers();

		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	/**
	 * Add a customer
	 * 
	 * @param customer
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/newCustomer",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new customer")
	public ResponseEntity<Customer> addCustomer(@RequestBody CustomerView customerView) throws Exception {

		Customer savedCustomer = new Customer();

		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<CustomerView>> violations = validator.validate(customerView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<CustomerView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//generate id for code
			UUID uuid = Generators.timeBasedGenerator().generate();
			String customerCode = uuid.toString();

			//set data to entity
			savedCustomer.setFirstName(customerView.getFirstName());
			savedCustomer.setLastName(customerView.getLastName());
			savedCustomer.setEmail(customerView.getEmail());
			savedCustomer.setPhone(customerView.getPhone());
			savedCustomer.setCustomerCode(customerCode);

			try {
				logger.info("Saving Customer");
				custService.saveCustomer(savedCustomer);
				logger.info("Customer creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create customer");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<Customer>(savedCustomer, HttpStatus.CREATED);
	}

	/**
	 * Updates the customer
	 * 
	 * @param customer
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updateCustomer/{email}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a customer")
	public ResponseEntity<Customer> updateCustomer(@PathVariable String email, @RequestBody CustomerView customerView) throws Exception {

		Customer savedCustomer = new Customer();

		//find Customer by code
		Customer customerFindByEmail = custService.findByCustomerEmail(email);
		//null checking
		if(customerFindByEmail == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Customer");
		}
		
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<CustomerView>> violations = validator.validate(customerView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<CustomerView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//update data to entity
			customerFindByEmail.setFirstName(customerView.getFirstName());
			customerFindByEmail.setLastName(customerView.getLastName());
			customerFindByEmail.setPhone(customerView.getPhone());

			try {
				logger.info("Update Customer");
				savedCustomer = custService.updateCustomer(customerFindByEmail);
				logger.info("Customer updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update customer");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}

		return new ResponseEntity<Customer>(savedCustomer, HttpStatus.OK);
	}

	/**
	 * Deletes customer identified with <code>id</code>
	 * @param id
	 * @throws Exception 
	 */
	@RequestMapping(path = "/deleteCustomer/{email}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a customer")
	public ResponseEntity<?> deletecustomers(@PathVariable String email) throws Exception {

		//find Customer by code
		Customer customerFindByEmail = custService.findByCustomerEmail(email);
		//null checking
		if(customerFindByEmail == null) {
			throw new WebserviceException(ErrorEnum.RECORD_NOT_FOUND, "Customer");
		}
		
		try {
			logger.info("Delete customers");
			custService.deleteCustomer(customerFindByEmail);
			logger.info("customers Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete customer");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
