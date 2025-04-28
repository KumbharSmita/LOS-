//package com.imperacred.BankLoanApplication.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.imperacred.BankLoanApplication.model.Otp;
//
//@Repository
//public interface OtpRepository extends JpaRepository<Otp, Integer> {
//	Optional<Otp> findByLeads_Id(Integer leadsId);
//
//}
//

package com.imperacred.BankLoanApplication.repository;

import com.imperacred.BankLoanApplication.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer> {
	Optional<Otp> findByLeads_LeadId(Integer leadId); // Assuming 'LeadId' is the field name in Leads class

}

