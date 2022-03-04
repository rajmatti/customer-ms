package com.coforge.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.coforge.training.model.Customer;
import com.coforge.training.model.Product;
import com.coforge.training.repository.CustomerRepo;

@Service
@Transactional
public class CustomerService {
	
	@Autowired
	private ProductConsumer consumer;
	
	@Autowired
	private CustomerRepo repo;
	
	public List<Customer> getCustomers(){
		return repo.findAll();
	}
	
	public List<Customer> saveCustomer(Customer c){
		repo.save(c);
		return repo.findAll();
	}
	
	public Customer getCustomer(Long pId) {
		return repo.findById(pId).get();
	}
	
	public Customer updateCustomer(Long id,Customer c) {
		return repo.save(c);
	}
	
	public void deleteCustomer(Long id) {
		repo.deleteById(id);
	}
}
