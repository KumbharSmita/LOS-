//package com.imperacred.BankLoanApplication.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.imperacred.BankLoanApplication.model.Kyc;
//import com.imperacred.BankLoanApplication.model.Leads;
//import com.imperacred.BankLoanApplication.repository.KycRepository;
//import com.imperacred.BankLoanApplication.repository.LeadsRepository;
//import com.imperacred.BankLoanApplication.service.KycService;
//
//import java.util.Optional;
//
//@Service
//public class KycServiceImpl implements KycService {
//
//    @Autowired
//    private KycRepository kycRepository;
//
//    @Autowired
//    private LeadsRepository leadsRepository;
//
//    @Override
//    public boolean verifyKyc(String leadsId, String aadharNumber, String panNumber) {
//        // Find the KYC record based on leadsId
//        Kyc kyc = kycRepository.findByLead_LeadsId(leadsId);
//        
//        // If KYC record is not found
//        if (kyc == null) {
//            return false;  // KYC record does not exist for the lead
//        }
//        
//        // Verify the provided Aadhar and PAN numbers
//        if (!kyc.getAadharNumber().equals(aadharNumber) || !kyc.getPanNumber().equals(panNumber)) {
//            return false;  // Provided Aadhar or PAN does not match the KYC record
//        }
//
//        // Update the KYC status to "VERIFIED"
//        kyc.setEkycStatus("VERIFIED");
//        kycRepository.save(kyc);  // Save the updated KYC status
//
//        // Update the lead's status to "KYC VERIFIED"
//        Optional<Leads> leadOptional = leadsRepository.findByLeadsId(leadsId);
//        if (leadOptional.isPresent()) {
//            Leads lead = leadOptional.get();
//            lead.setStatus("KYC VERIFIED");
//            leadsRepository.save(lead);  // Save the updated lead status
//        }
//
//        return true;  // Successfully verified the KYC
//    }
//}
