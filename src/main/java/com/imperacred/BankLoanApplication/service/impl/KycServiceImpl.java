package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.KycDTO;
import com.imperacred.BankLoanApplication.model.Kyc;
import com.imperacred.BankLoanApplication.model.Leads;
import com.imperacred.BankLoanApplication.repository.KycRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.KycService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KycServiceImpl implements KycService {

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private KycRepository kycRepository;

    @Override
    public String submitKyc(KycDTO dto) {
        Leads lead = leadsRepository.findById(dto.getLeadsId())
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + dto.getLeadsId()));

        Kyc kyc = new Kyc();
        kyc.setLead(lead);
        kyc.setAadharNumber(dto.getAadharNumber());
        kyc.setPanNumber(dto.getPanNumber());
        kyc.setEkycStatus(dto.getEkycStatus());
        kyc.setVerifiedAt(LocalDateTime.now());

        kycRepository.save(kyc);

        return "KYC details saved successfully for lead ID: " + dto.getLeadsId();
    }
    
    // Get KYC by Lead ID
    @Override
    public KycDTO getKycByLeadId(Integer leadId) {
        Kyc kyc = kycRepository.findByLead_LeadId(leadId)
                .orElseThrow(() -> new RuntimeException("KYC not found for Lead ID: " + leadId));

        return new KycDTO(
                kyc.getLead().getLeadId(),
                kyc.getAadharNumber(),
                kyc.getPanNumber(),
                kyc.getEkycStatus(),
                kyc.getVerifiedAt()
        );
    }

    // Delete KYC by KYC ID
    @Override
    public String deleteKycById(Integer kycId) {
        Optional<Kyc> kyc = kycRepository.findById(kycId);
        if (kyc.isPresent()) {
            kycRepository.deleteById(kycId);
            return "KYC deleted successfully for ID: " + kycId;
        } else {
            throw new RuntimeException("KYC not found with ID: " + kycId);
        }
    }
}
