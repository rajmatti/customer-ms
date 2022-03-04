package com.coforge.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coforge.training.model.Product;
import com.coforge.training.repository.ProductRepo;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	private ProductRepo repo;
	
	public List<Product> getProducts(){
		return repo.findAll();
	}
	
	public List<Product> saveProduct(Product p){
		repo.save(p);
		return repo.findAll();
	}
	
	public Product get(Long id) {
		return repo.findById(id).get();
	}
	
	public Product updateProduct(Long id,Product p) {
		return repo.save(p);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}
}
