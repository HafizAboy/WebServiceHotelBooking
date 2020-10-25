package com.webservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservice.model.Customer;
import com.webservice.repository.CustomerRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
	private CustomerRepository custRepo;

	@Override
	public Customer findById(UUID id) {
		// TODO Auto-generated method stub
		return custRepo.getOne(id);
	}

	@Override
	public Customer findByCustomerEmail(String email) {
		// TODO Auto-generated method stub
		return custRepo.findByEmail(email);
	}

	@Override
	public List<Customer> findAllCustomers() {
		// TODO Auto-generated method stub
		return (List<Customer>)custRepo.findAll();
	}

	@Override
	public Customer saveCustomer(Customer customer) throws Exception {
		// TODO Auto-generated method stub
		return custRepo.save(customer);
	}

	@Override
	public Customer updateCustomer(Customer customer) throws Exception {
		// TODO Auto-generated method stub
		return saveCustomer(customer);
	}

	@Override
	public void deleteCustomer(Customer customer) throws Exception {
		// TODO Auto-generated method stub
		custRepo.delete(customer);
	}

	@Override
	public Customer findByCustomerCode(String customerCode) {
		// TODO Auto-generated method stub
		return custRepo.findByCustomerCode(customerCode);
	}

}
