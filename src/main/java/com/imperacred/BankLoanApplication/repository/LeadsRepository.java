package com.imperacred.BankLoanApplication.repository;

import com.imperacred.BankLoanApplication.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadsRepository extends JpaRepository<Lead, Integer> {

    // Find a lead by email address
    Optional<Lead> findByEmail(String email);

    // Find a lead by phone number
    Optional<Lead> findByPhone(String phone);
<<<<<<< HEAD

    // Find a lead by Aadhaar number
    Optional<Lead> findByAadhaarNumber(String aadhaarNumber);

    // Find a lead by PAN number
    Optional<Lead> findByPanNumber(String panNumber);

    // Check if any lead exists with same email, phone, Aadhaar, or PAN
    boolean existsByEmailOrPhoneOrAadhaarNumberOrPanNumber(String email, String phone, String aadhaarNumber, String panNumber);

    // Get the maximum leadsId for generating displayId (if needed)
    @Query("SELECT MAX(l.leadsId) FROM Lead l")
    Integer findMaxLeadsId();

	Optional<Lead> findById(Integer leadsId);
=======

    // Find a lead by Aadhaar number
    Optional<Lead> findByAadhaarNumber(String aadhaarNumber);

    // Find a lead by PAN number
    Optional<Lead> findByPanNumber(String panNumber);

    // Check if any lead exists with same email, phone, Aadhaar, or PAN
    boolean existsByEmailOrPhoneOrAadhaarNumberOrPanNumber(String email, String phone, String aadhaarNumber, String panNumber);

    // Get the maximum leadsId for generating displayId (if needed)
    @Query("SELECT MAX(l.leadsId) FROM Lead l")
    Integer findMaxLeadsId();

	Optional<Lead> findById(Integer leadsId);
	
 

>>>>>>> origin/feature2
}
