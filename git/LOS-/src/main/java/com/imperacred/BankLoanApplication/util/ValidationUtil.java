package com.imperacred.BankLoanApplication.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    // Validate email format using regex
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Validate Aadhaar number (should be exactly 12 digits)
    public static boolean isValidAadhaarNumber(String aadhaar) {
        return aadhaar != null && aadhaar.matches("\\d{12}");
    }

    // Validate phone number (should be exactly 10 digits)
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }
    
    public static boolean isValidPanNumber(String panNumber) {
        if (panNumber == null) {
            return false;
        }
        String panRegex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        Pattern pattern = Pattern.compile(panRegex);
        Matcher matcher = pattern.matcher(panNumber);
        return matcher.matches();
    }
    
    public static boolean isValidBankAccount(String account) {
        return account != null && account.matches("\\d{9,18}");
    }
}
