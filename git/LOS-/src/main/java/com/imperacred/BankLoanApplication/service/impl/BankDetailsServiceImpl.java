package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.BankDetailsDTO;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.BankDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankDetailsServiceImpl implements BankDetailsService {

    private static final Logger logger = LogManager.getLogger(BankDetailsServiceImpl.class);

    @Autowired
    private LeadsRepository leadsRepository;

    @Override
    public BankDetailsDTO saveBankDetails(Integer leadsId, BankDetailsDTO dto) {
        logger.info("Saving bank details for Lead ID: {}", leadsId);

        Lead lead = leadsRepository.findById(leadsId)
                .orElseThrow(() -> {
                    logger.error("Lead not found with ID: {}", leadsId);
                    return new RuntimeException("Lead not found with ID: " + leadsId);
                });

        logger.debug("Found lead: {}", lead.getEmail());

        if (lead.getConfirmedAmount() == null || lead.getConfirmedTenureMonths() == null) {
            logger.warn("Loan not confirmed yet for Lead ID: {}. Cannot proceed with bank details submission.", leadsId);
            throw new IllegalStateException("Loan not confirmed yet. Cannot add bank details.");
        }

        logger.info("Updating bank details for Lead ID: {}", leadsId);
        lead.setBankAccountHolderName(dto.getAccountHolderName());
        lead.setBankAccountNumber(dto.getAccountNumber());
        lead.setBankIfscCode(dto.getIfscCode());

        leadsRepository.save(lead);
        logger.info("Bank details successfully saved for Lead ID: {}", leadsId);

        dto.setLeadsId(leadsId);
        return dto;
    }
}
