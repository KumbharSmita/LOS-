package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.service.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);  // Logger for the class

    @Autowired
    private JavaMailSender javaMailSender;  

    @Override
    public void sendEmail(String toEmail, String subject, String message) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(toEmail);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        try {
            javaMailSender.send(emailMessage);  // Sending the email
            logger.info("Email sent successfully to: {}", toEmail);  // Logging success
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", toEmail, e);  // Logging failure with exception details
        }
    }

    @Override
    public void sendOtpEmail(String email, String otp) {
        String subject = "Your OTP for Bank Loan Application";
        String message = "Dear Customer,\n\n" +
                         "Your OTP for verifying your loan enquiry is: " + otp + "\n\n" +
                         "Please enter this OTP within the next 2 minutes to complete the verification.\n\n" +
                         "Thank you.\n\n" +
                         "Best regards,\n" +
                         " Imperacred Bank Loan Application Team";

        try {
            sendEmail(email, subject, message);  // Reusing the sendEmail method
            logger.info("OTP sent successfully to: {}", email);  // Logging OTP sent success
        } catch (Exception e) {
            logger.error("Error sending OTP email to: {}", email, e);  // Logging OTP email sending failure
        }
    }

    @Override
    public void sendSimpleEmail(String email, String subject, String body) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);            // Set recipient's email
        emailMessage.setSubject(subject);    // Set the subject of the email
        emailMessage.setText(body);          // Set the body/content of the email

        try {
            javaMailSender.send(emailMessage);  // Send the email using JavaMailSender
            logger.info(" email sent successfully to: {}", email);  // Log success
        } catch (Exception e) {
            logger.error("Failed to send  email to: {}", email, e);  // Log error with exception details
        }
    }
    @Override
    public void sendDisbursementOtpEmail(String email, String name, String otp, boolean isResend) {
        String subject = isResend ? "Your OTP for Loan Disbursement - Resent" : "Your OTP for Loan Disbursement";
        
        String message = "Dear " + name + ",\n\n"
                + "Thank you for choosing Imperacred for your loan needs.\n\n"
                + "To proceed with the disbursement of your approved loan, please use the following  (OTP):\n\n"
                + " OTP: " + otp + "\n\n"
                + "This OTP is valid for the next 2 minutes only. Please do not share it with anyone.\n\n"
                + "If you did not request this OTP, please contact our support team immediately.\n\n"
                + "Best regards,\n"
                + "Imperacred Bank Loan Application Team";

        sendEmail(email, subject, message);
    }


}
