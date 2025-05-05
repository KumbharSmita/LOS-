package com.imperacred.BankLoanApplication.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
import com.imperacred.BankLoanApplication.repository.UnderwritingResultsRepository;
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

@Service
public class UnderwritingResultsServiceImpl implements UnderwritingResultsService{

    @Autowired
    private UnderwritingResultsRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    public UnderwritingResults saveResult(UnderwritingResultsDTO dto) {
        UnderwritingResults result = new UnderwritingResults();
        result.setApplicationId(dto.getApplicationId());
        result.setUnderwriterNotes(dto.getUnderwriterNotes());
        result.setApprovedAmount(dto.getApprovedAmount());

        int creditScore = dto.getCreditScore();
        String decision;
        String riskRating;

        if (creditScore >= 700) {
            decision = "Approved";
            riskRating = "Low";
        } else if (creditScore >= 600) {
            decision = "Approved";
            riskRating = "Medium";
        } else {
            decision = "Rejected";
            riskRating = "High";

            // Send Rejection Email
            String rejectionMessage = "Dear Applicant,\n\nWe regret to inform you that your loan application has been rejected due to a low credit score (" + creditScore + ").\n\nRegards,\nLoan Underwriting Team";
            sendRejectionEmail(dto.getEmail(), rejectionMessage);
        }

        result.setDecision(decision);
        result.setRiskRating(riskRating);

        return repository.save(result);
    }
    
    @Override
    public List<UnderwritingResults> getAllResults() {
        return repository.findAll();
    }

    @Override
    public UnderwritingResults getResultById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Result not found with ID: " + id));
    }

    @Override
    public UnderwritingResults updateResult(Integer id, UnderwritingResultsDTO dto) {
        UnderwritingResults result = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Result not found with ID: " + id));

        result.setApplicationId(dto.getApplicationId());
        result.setUnderwriterNotes(dto.getUnderwriterNotes());
        result.setApprovedAmount(dto.getApprovedAmount());
        result.setDecision(dto.getDecision());
        result.setRiskRating(dto.getRiskRating());

        return repository.save(result);
    }

    @Override
    public String deleteResult(Integer id) {
        repository.deleteById(id);
        return "Underwriting result deleted with ID: " + id;
    }

    //method for sending email of approval / rejection
    private void sendRejectionEmail(String to, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Loan Application Rejected");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}


