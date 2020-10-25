package com.webservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webservice.model.Payment;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	Payment findByTransactionCode(String transactionCode);
	
}
