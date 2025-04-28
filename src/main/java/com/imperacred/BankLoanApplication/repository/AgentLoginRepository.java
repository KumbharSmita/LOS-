package com.imperacred.BankLoanApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imperacred.BankLoanApplication.model.AgentLogin;

@Repository
public interface AgentLoginRepository extends JpaRepository<AgentLogin, Integer> {

   
	Optional<AgentLogin> findByEmailAndPassword(String email, String password);  
}
