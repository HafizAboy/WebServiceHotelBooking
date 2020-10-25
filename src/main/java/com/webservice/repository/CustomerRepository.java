package com.webservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webservice.model.Customer;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	Customer findByCustomerCode(String customerCode);
	Customer findByEmail(String email);
	
}
