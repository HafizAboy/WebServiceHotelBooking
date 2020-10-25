package com.webservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservice.model.Payment;
import com.webservice.repository.PaymentRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
	private PaymentRepository pymtRepo;

	@Override
	public Payment findById(UUID id) {
		// TODO Auto-generated method stub
		return pymtRepo.getOne(id);
	}
	
	@Override
	public List<Payment> findAllPayments() {
		// TODO Auto-generated method stub
		return (List<Payment>)pymtRepo.findAll();
	}

	@Override
	public Payment savePayment(Payment payment) throws Exception {
		// TODO Auto-generated method stub
		return pymtRepo.save(payment);
	}

	@Override
	public Payment updatePayment(Payment payment) throws Exception {
		// TODO Auto-generated method stub
		return savePayment(payment);
	}

	@Override
	public void deletePayment(Payment payment) throws Exception {
		// TODO Auto-generated method stub
		pymtRepo.delete(payment);
	}

	@Override
	public Payment findByPaymentCode(String transactionCode) {
		// TODO Auto-generated method stub
		return pymtRepo.findByTransactionCode(transactionCode);
	}

}
