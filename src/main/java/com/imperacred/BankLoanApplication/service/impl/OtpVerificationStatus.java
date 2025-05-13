
package com.imperacred.BankLoanApplication.service.impl;

public enum OtpVerificationStatus {
    SUCCESS("OTP verified successfully"),
    INVALID("Invalid OTP"),
    EXPIRED("OTP expired"),
    NOT_FOUND("No OTP found");

    private final String message;

    OtpVerificationStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/feature2
