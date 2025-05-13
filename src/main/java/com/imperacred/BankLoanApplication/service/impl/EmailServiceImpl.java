
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
    private JavaMailSender javaMailSender;  // Used to send email via SMTP

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
                         "Your OTP for verifying your loan application is: " + otp + "\n\n" +
                         "Please enter this OTP within the next 2 minutes to complete the verification.\n\n" +
                         "Thank you.\n\n" +
                         "Best regards,\n" +
                         "Bank Loan Application Team";

        try {
            sendEmail(email, subject, message);  // Reusing the sendEmail method
            logger.info("OTP sent successfully to: {}", email);  // Logging OTP sent success
        } catch (Exception e) {
            logger.error("Error sending OTP email to: {}", email, e);  // Logging OTP email sending failure
        }
    }
}
