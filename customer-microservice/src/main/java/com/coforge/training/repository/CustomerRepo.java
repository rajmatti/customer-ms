package com.coforge.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

}
