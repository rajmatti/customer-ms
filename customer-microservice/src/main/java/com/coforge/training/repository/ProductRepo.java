package com.coforge.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}
