package com.webservice.service;

import java.util.List;
import java.util.UUID;

import com.webservice.model.Customer;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface CustomerService {

	Customer findById(UUID id);
	Customer findByCustomerCode(String customerCode);
	Customer findByCustomerEmail(String email);
    List<Customer> findAllCustomers();
	Customer saveCustomer(Customer customer) throws Exception;
	Customer updateCustomer(Customer customer) throws Exception;
	void deleteCustomer(Customer customer) throws Exception;
}
