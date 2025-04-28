package com.imperacred.BankLoanApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imperacred.BankLoanApplication.model.Agents;
@Repository
public interface AgentsRepository extends JpaRepository<Agents,Integer> {

}
