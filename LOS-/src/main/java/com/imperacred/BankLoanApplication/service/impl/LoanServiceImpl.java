package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.model.RepaymentSchedule;
import com.imperacred.BankLoanApplication.dto.DisbursementsDTO;
import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.repository.RepaymentScheduleRepository;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.repository.EmiRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.LoanService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private static final Logger logger = LogManager.getLogger(LoanServiceImpl.class);

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private EmiRepository emiRepository;

    @Autowired
    private DisbursementsRepository disbursementRepository;

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @Override
    public BigDecimal calculateEMI(BigDecimal principal, int tenureMonths, BigDecimal annualInterestRate) {
        logger.debug("Calculating EMI for principal: {} for tenure: {} months with annual interest rate: {}",
                principal, tenureMonths, annualInterestRate);

        BigDecimal monthlyInterestRate = annualInterestRate
                .divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        // EMI formula
        BigDecimal emiAmount = principal.multiply(monthlyInterestRate)
                .multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(tenureMonths))
                .divide(BigDecimal.ONE.add(monthlyInterestRate).pow(tenureMonths).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        logger.debug("Calculated EMI: {}", emiAmount);
        return emiAmount;
    }

    @Transactional
    @Override
    public void createEMISchedule(Lead lead, BigDecimal emiAmount, Disbursements disbursements) {
        logger.info("Creating EMI schedule for lead ID: {}", lead.getLeadsId());

        Integer confirmedTenure = lead.getConfirmedTenureMonths();
        if (confirmedTenure == null || confirmedTenure <= 0) {
            throw new IllegalArgumentException("Confirmed tenure must be a positive number.");
        }

        int totalInstallments = confirmedTenure;
        LocalDate startDate = LocalDate.now().plusMonths(1);
        LocalDate endDate = startDate.plusMonths(totalInstallments - 1);

        RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
        repaymentSchedule.setLeadsId(lead.getLeadsId());
        repaymentSchedule.setTotalInstallments(totalInstallments);
        repaymentSchedule.setStartDate(startDate);
        repaymentSchedule.setEndDate(endDate);

        BigDecimal totalAmount = emiAmount.multiply(BigDecimal.valueOf(totalInstallments));
        BigDecimal totalInterest = totalAmount.subtract(disbursements.getApprovedAmount());
        repaymentSchedule.setTotalAmount(totalAmount);
        repaymentSchedule.setTotalInterest(totalInterest);
        repaymentSchedule.setCreatedAt(LocalDateTime.now());
        repaymentSchedule.setUpdatedAt(LocalDateTime.now());

        repaymentScheduleRepository.save(repaymentSchedule);
        logger.info("EMI schedule created with schedule ID: {}", repaymentSchedule.getScheduleId());

        int scheduleId = repaymentSchedule.getScheduleId();
        for (int i = 0; i < totalInstallments; i++) {
            Emi emi = new Emi();
            emi.setLeadsId(lead.getLeadsId());
            emi.setSchedule_id(scheduleId);
            emi.setEmi_amount(emiAmount);
            emi.setDue_date(startDate.plusMonths(i));
            emi.setPaid_date(null);
            emi.setStatus("PENDING"); // You can replace with EmiStatus.PENDING.name() if using enum
            emi.setCreated_at(LocalDateTime.now());
            emi.setUpdated_at(LocalDateTime.now());

            emiRepository.save(emi);
            logger.debug("EMI for installment {} created with amount: {}", i + 1, emiAmount);
        }
    }

    @Transactional
    @Override
    public void processLoanApplication(Lead lead) {
        logger.info("Processing loan with Lead ID: {}", lead.getLeadsId());

        try {
            Disbursements disbursement = disbursementRepository.findByLeadsId(lead.getLeadsId())
                    .orElseThrow(() -> new RuntimeException("Disbursement not found for application ID: " + lead.getLeadsId()));

            BigDecimal baseInterestRate = disbursement.getRateOfInterest();
            if (baseInterestRate == null) {
                throw new RuntimeException("Rate of interest is not set for disbursement with lead ID: " + lead.getLeadsId());
            }

            BigDecimal principal = disbursement.getApprovedAmount();
            logger.debug("Fetched disbursement amount for lead ID {}: {}", lead.getLeadsId(), principal);

            Integer confirmedTenure = lead.getConfirmedTenureMonths();
            if (confirmedTenure == null || confirmedTenure <= 0) {
                throw new IllegalArgumentException("Confirmed tenure must be a positive number.");
            }

            BigDecimal emiAmount = calculateEMI(principal, confirmedTenure, baseInterestRate);

            createEMISchedule(lead, emiAmount, disbursement);
            logger.info("Lead with ID: {} processed successfully.", lead.getLeadsId());

        } catch (Exception e) {
            logger.error("Error occurred while processing loan application ID: {}", lead.getLeadsId(), e);
            throw e;
        }
    }

    @Override
    public List<Emi> getEmisByLeadsId(Integer leadsId) {
        logger.info("Fetching EMI details for leads ID: {}", leadsId);
        List<Emi> emis = emiRepository.findByLeadsId(leadsId);
        logger.debug("Fetched {} EMI records for lead ID: {}", emis.size(), leadsId);
        return emis;
    }
}
