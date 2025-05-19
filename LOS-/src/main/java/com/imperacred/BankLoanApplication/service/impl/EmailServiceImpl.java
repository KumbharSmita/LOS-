package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.service.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of EmailService using Spring's JavaMailSender.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);  // Logger instance

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String toEmail, String subject, String message) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(toEmail);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        try {
            javaMailSender.send(emailMessage);
            logger.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", toEmail, e);
        }
    }

    @Override
    public void sendOtpEmail(String email, String otp) {
        String subject = "Your OTP for Bank Loan Application";
        String message = "Dear Customer,\n\n" +
                         "Your OTP for verifying your loan application is: " + otp + "\n\n" +
                         "Please enter this OTP within the next 2 minutes.\n\n" +
                         "Best regards,\n" +
                         "Bank Loan Application Team";

        sendEmail(email, subject, message);
        logger.info("OTP email sent to: {}", email);
    }

    @Override
    public void sendDocumentUploadConfirmation(String email, String applicantName) {
        String subject = "Document Upload Successful";
        String message = "Dear " + applicantName + ",\n\n" +
                         "We have successfully received your loan documents.\n" +
                         "Our team will now begin the review process.\n\n" +
                         "Thank you for choosing us.\n\n" +
                         "Regards,\nBank Loan Application Team";

        sendEmail(email, subject, message);
        logger.info("Document upload confirmation email sent to: {}", email);
    }

    @Override
    public void sendLoanApprovalEmail(String email, String applicantName, String loanAmount, String approvedDate) {
        String subject = "Congratulations! Your Loan is Approved";
        String message = "Dear " + applicantName + ",\n\n" +
                         "We are pleased to inform you that your loan of amount ₹" + loanAmount + 
                         " has been approved on " + approvedDate + ".\n\n" +
                         "Please check your bank account for disbursement details or contact your loan officer for more information.\n\n" +
                         "Warm regards,\nBank Loan Application Team";

        sendEmail(email, subject, message);
        logger.info("Loan approval email sent to: {}", email);
    }

    @Override
    public void sendLoanRejectionEmail(String email, String applicantName, String reason) {
        String subject = "Loan Application Update – Rejected";
        String message = "Dear " + applicantName + ",\n\n" +
                         "We regret to inform you that your loan application has been rejected.\n" +
                         "Reason: " + reason + "\n\n" +
                         "You may reapply after resolving the issue or contact support for clarification.\n\n" +
                         "Regards,\nBank Loan Application Team";

        sendEmail(email, subject, message);
        logger.info("Loan rejection email sent to: {}", email);
    }
}
