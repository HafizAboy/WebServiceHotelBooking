package com.webservice.service;

import java.util.List;
import java.util.UUID;

import com.webservice.model.Payment;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface PaymentService {

	Payment findById(UUID id);
	Payment findByPaymentCode(String paymentCode);
    List<Payment> findAllPayments();
	Payment savePayment(Payment payment) throws Exception;
	Payment updatePayment(Payment payment) throws Exception;
	void deletePayment(Payment payment) throws Exception;
}
