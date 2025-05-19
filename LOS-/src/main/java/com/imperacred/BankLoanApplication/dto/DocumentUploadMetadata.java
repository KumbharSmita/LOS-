package com.imperacred.BankLoanApplication.dto;

public class DocumentUploadMetadata {
    private String documentType;
    private String documentCategory;

    // Getters & Setters
    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentCategory() {
        return documentCategory;
    }
    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }
}
