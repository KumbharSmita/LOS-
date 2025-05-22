package com.imperacred.BankLoanApplication.mapper;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.model.Lead;

public class LeadMapper {

	    public static Lead toEntity(LeadsDTO dto) {
	        Lead lead = new Lead();
	        lead.setFirstName(dto.getFirstName());
	        lead.setLastName(dto.getLastName());
	        lead.setEmail(dto.getEmail());
	        lead.setPhone(dto.getPhone());
	        lead.setPanNumber(dto.getPanNumber());
	        lead.setAadhaarNumber(dto.getAadhaarNumber());
	        lead.setSource(dto.getSource());
	        lead.setLoanType(dto.getLoanType());
	        lead.setAmount(dto.getAmount());
	        lead.setTenureMonths(dto.getTenureMonths());
	        lead.setPurpose(dto.getPurpose());
	        return lead;
	    }

	    public static LeadsDTO toDto(Lead lead) {
	        LeadsDTO dto = new LeadsDTO();
	        dto.setLeadsId(lead.getLeadsId());
	        dto.setFirstName(lead.getFirstName());
	        dto.setLastName(lead.getLastName());
	        dto.setEmail(lead.getEmail());
	        dto.setPhone(lead.getPhone());
	        dto.setPanNumber(lead.getPanNumber());
	        dto.setAadhaarNumber(lead.getAadhaarNumber());
	        dto.setSource(lead.getSource());
	        dto.setLoanType(lead.getLoanType());
	        dto.setAmount(lead.getAmount());
	        dto.setTenureMonths(lead.getTenureMonths());
	        dto.setPurpose(lead.getPurpose());
	       
	        return dto;
	    }
	}


