package com.imperacred.BankLoanApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.Emi;

public interface EmiRepository extends JpaRepository<Emi,Integer>{

List<Emi> findByLeadsId(Integer leadsId);

}

