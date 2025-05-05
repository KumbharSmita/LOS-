package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.*;
import com.imperacred.BankLoanApplication.model.Leads;
import com.imperacred.BankLoanApplication.model.Otp;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.OtpRepository;
import com.imperacred.BankLoanApplication.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private static final long OTP_EXPIRATION_SECONDS = 90;

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public OtpDTO generateOtp(OtpRequest request) {
        String otpValue = String.valueOf(new Random().nextInt(900000) + 100000);// it can generates 6 digit otp
        Timestamp expiryTime = Timestamp.valueOf(LocalDateTime.now().plusSeconds(OTP_EXPIRATION_SECONDS));

        Leads lead = leadsRepository.findById(request.getLeads_id()) // fetch the lead by id
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + request.getLeads_id()));

        Optional<Otp> optionalOtp = otpRepository.findByLeads_LeadId(lead.getLeadId());// finds existing otp for lead
        Otp otp = optionalOtp.orElse(new Otp());

        otp.setLeads(lead);
        otp.setOtpValue(otpValue);
        otp.setExpiryTime(expiryTime);
        otp.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        if (otp.getCreatedAt() == null) {
            otp.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        }

        otpRepository.save(otp);//saving data to db

        //  Send email instead of printing
        sendOtpEmail(lead.getEmail(), otpValue);
        
        //return response to dto
        return new OtpDTO(request.getLeads_id(), "OTP sent successfully to email.");
    }

    @Override
    public OtpDTO verifyOtp(OtpVerificationRequest request) {
        Leads lead = leadsRepository.findById(request.getLeadsId())
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + request.getLeadsId()));

        Optional<Otp> otpOptional = otpRepository.findByLeads_LeadId(lead.getLeadId());

        if (otpOptional.isEmpty()) {
            return new OtpDTO(request.getLeadsId(), "OTP not found for this lead.");
        }

        Otp otp = otpOptional.get();

        if (otp.getExpiryTime().before(Timestamp.valueOf(LocalDateTime.now()))) {
            return new OtpDTO(request.getLeadsId(), "OTP has expired.");//current time after expiry time
        }

        if (!otp.getOtpValue().equals(request.getOtpValue())) {
            return new OtpDTO(request.getLeadsId(), "Invalid OTP.");//otp dosent match stored value
        }

        return new OtpDTO(request.getLeadsId(), "OTP verified successfully.");
    }

    private void sendOtpEmail(String toEmail, String otpValue) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Verification");
        message.setText("Your One-Time Password (OTP) is: " + otpValue + "\n\nIt is valid for 5 minutes.");
        message.setFrom("your-email@gmail.com"); // Optional if configured globally

        mailSender.send(message);
    }
}



