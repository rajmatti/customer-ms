package com.coforge.training.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.coforge.training.model.Product;

@FeignClient(name="product-service")
public interface ProductConsumer {
	
	@GetMapping("/products/all")
	public List<Product> getProducts();
	
	@GetMapping(value = "products/get/{id}")
	public Product getProduct(@PathVariable("id") Long pId);
	
	@PostMapping("/products")
	public List<Product> addNewProduct(@RequestBody Product product);
	
	@PutMapping("products/update/{id}")
	public Product updateProduct(@PathVariable("id") Long pId,@RequestBody Product pro);
	
	@DeleteMapping("products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long pId);
	
	
}


