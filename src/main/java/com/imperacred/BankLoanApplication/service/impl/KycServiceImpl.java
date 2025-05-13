//package com.imperacred.BankLoanApplication.service.impl;
//
<<<<<<< HEAD
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
=======
//import com.imperacred.BankLoanApplication.dto.KycDTO;
>>>>>>> origin/feature2
//import com.imperacred.BankLoanApplication.model.Kyc;
//import com.imperacred.BankLoanApplication.model.Leads;
//import com.imperacred.BankLoanApplication.repository.KycRepository;
//import com.imperacred.BankLoanApplication.repository.LeadsRepository;
//import com.imperacred.BankLoanApplication.service.KycService;
<<<<<<< HEAD
//
=======
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
>>>>>>> origin/feature2
//import java.util.Optional;
//
//@Service
//public class KycServiceImpl implements KycService {
//
//    @Autowired
<<<<<<< HEAD
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
=======
//    private LeadsRepository leadsRepository;
//
//    @Autowired
//    private KycRepository kycRepository;
//
//    @Override
//    public String submitKyc(KycDTO dto) {
//        Leads lead = leadsRepository.findById(dto.getLeadsId())
//                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + dto.getLeadsId()));
//
//        Kyc kyc = new Kyc();
//        kyc.setLead(lead);
//        kyc.setAadharNumber(dto.getAadharNumber());
//        kyc.setPanNumber(dto.getPanNumber());
//        kyc.setEkycStatus(dto.getEkycStatus());
//        kyc.setVerifiedAt(LocalDateTime.now());
//
//        kycRepository.save(kyc);
//
//        return "KYC details saved successfully for lead ID: " + dto.getLeadsId();
//    }
//    
//    // Get KYC by Lead ID
//    @Override
//    public KycDTO getKycByLeadId(Integer leadId) {
//        Kyc kyc = kycRepository.findByLead_LeadId(leadId)
//                .orElseThrow(() -> new RuntimeException("KYC not found for Lead ID: " + leadId));
//
//        return new KycDTO(
//                kyc.getLead().getLeadId(),
//                kyc.getAadharNumber(),
//                kyc.getPanNumber(),
//                kyc.getEkycStatus(),
//                kyc.getVerifiedAt()
//        );
//    }
//
//    // Delete KYC by KYC ID
//    @Override
//    public String deleteKycById(Integer kycId) {
//        Optional<Kyc> kyc = kycRepository.findById(kycId);
//        if (kyc.isPresent()) {
//            kycRepository.deleteById(kycId);
//            return "KYC deleted successfully for ID: " + kycId;
//        } else {
//            throw new RuntimeException("KYC not found with ID: " + kycId);
//        }
>>>>>>> origin/feature2
//    }
//}
