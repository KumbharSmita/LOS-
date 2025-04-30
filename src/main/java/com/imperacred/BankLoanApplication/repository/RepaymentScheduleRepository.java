package com.imperacred.BankLoanApplication.repository;



import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.RepaymentSchedule;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule,Integer> {

	Optional findByApplicationId(Integer applicationId);


	

}
