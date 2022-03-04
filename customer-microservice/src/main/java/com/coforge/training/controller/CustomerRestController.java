package com.coforge.training.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.training.model.Customer;
import com.coforge.training.model.Product;
import com.coforge.training.repository.CustomerRepo;
import com.coforge.training.service.CustomerService;
import com.coforge.training.service.ProductConsumer;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/customer")
public class CustomerRestController {
	
	@Autowired
	private ProductConsumer consumer;
	
	@Autowired
	private CustomerService service;
	
	@Autowired
	private CustomerRepo repo;
	
	@HystrixCommand(fallbackMethod = "fallback")
	@GetMapping(value="/all")
	public ResponseEntity<List<Customer>> findProducts(){
		return new ResponseEntity<List<Customer>>(service.getCustomers(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<Customer> getProduct(@PathVariable("id") Long pId){
		return new ResponseEntity<>(service.getCustomer(pId), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<List<Customer>> addNewCustomer(@RequestBody Customer customer){
		Product p = customer.getProduct();
		customer.setProduct(p);
		p.setCustomer(customer);
		consumer.addNewProduct(p);				//// Calling Parent
	return new ResponseEntity<>(service.saveCustomer(customer), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long pId,@RequestBody Customer customer){
		Customer c = repo.findById(pId).get();
		//Fetching Product ID to update in product microservice
		Product pro = c.getProduct();
		
		c.setCustName(customer.getCustName());
		
		pro.setProductName(customer.getProduct().getProductName());
		pro.setPrice(customer.getProduct().getPrice());
		
		c.setProduct(pro);
		pro.setCustomer(c);
		

		Product uProduct = consumer.updateProduct(pro.getProductId(), pro);			// Calling Parent
		uProduct.setCustomer(c);
		Customer updatedCustomer = service.updateCustomer(pId, c);
		return ResponseEntity.ok().body(updatedCustomer);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long pId){
		Product p = service.getCustomer(pId).getProduct();
		service.deleteCustomer(pId);
		consumer.deleteProduct(p.getProductId());				//Calling Parent
		return ResponseEntity.ok().body("Customer Deleted.");
	}
	
	@GetMapping(value = "/get/product/{id}")
	public Product getProductsOfCust(@PathVariable("id") Long pId){
		Customer c = repo.findById(pId).get();
		Long id = c.getProduct().getProductId();
		return consumer.getProduct(id);
	}
	
	@HystrixCommand(fallbackMethod = "callServiceAndGetData_Fallback")
	@GetMapping
	public ResponseEntity<String> getProducts(){
		List<Product> p = consumer.getProducts();
		return ResponseEntity.ok().body("Get Products");
	}
	
	public ResponseEntity<String> callServiceAndGetData_Fallback() {
		System.out.println("Product Service is down!!! fallback route enabled...");
		return ResponseEntity.ok().body("CIRCUIT BREAKER ENABLED!!! No Response From Product Service at this moment. Product Service is down!!! " +
		" Service will be back shortly - ");
		}
	
	public ResponseEntity<List<Customer>> fallback(){
		List l = new ArrayList<>();
		l.add("CIRCUIT BREAKER ENABLED!!! No Response From Product Service at this moment. Product Service is down!!!"
				+ " Service will be back shortly - ");
		return ResponseEntity.ok().body(l);
	}
}
