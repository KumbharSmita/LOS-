package com.imperacred.BankLoanApplication.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.model.Leads;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.LeadsService;

@Service
public class LeadsServiceImpl implements LeadsService {

    @Autowired
    private LeadsRepository leadsRepository;

    // Save Lead
    @Override
    public LeadsDTO saveLead(LeadsDTO leadsDto) {
        Leads lead = new Leads();
        lead.setLeadId(leadsDto.getLeads_id());
        lead.setFull_name(leadsDto.getFull_name());
        lead.setEmail(leadsDto.getEmail());
        lead.setPhone(leadsDto.getPhone());
        lead.setSource(leadsDto.getSource());
        lead.setStatus(leadsDto.getStatus());
        lead.setCreated_at(LocalDateTime.now());

        Leads saved = leadsRepository.save(lead);
        
        
        LeadsDTO dto = new LeadsDTO();
        dto.setLeads_id(saved.getLeadId());
        dto.setFull_name(saved.getFull_name());
        dto.setEmail(saved.getEmail());
        dto.setPhone(saved.getPhone());
        dto.setSource(saved.getSource());
        dto.setStatus(saved.getStatus());
        dto.setCreatedAt(saved.getCreated_at());

        return dto;
    }
   
    //get lead by id
    @Override
    public LeadsDTO getLeadById(Integer id) {
        Leads lead = leadsRepository.findById(id).orElse(null);
        if (lead == null) return null;

        LeadsDTO dto = new LeadsDTO();
        dto.setFull_name(lead.getFull_name());
        dto.setEmail(lead.getEmail());
        dto.setPhone(lead.getPhone());
        dto.setSource(lead.getSource());
        dto.setStatus(lead.getStatus());
        dto.setCreatedAt(lead.getCreated_at());

        return dto;
    }
    
    //get all leads
    @Override
    public List<LeadsDTO> getAllLeads() {
        return leadsRepository.findAll().stream().map(lead -> {
            LeadsDTO dto = new LeadsDTO();
            dto.setFull_name(lead.getFull_name());
            dto.setEmail(lead.getEmail());
            dto.setPhone(lead.getPhone());
            dto.setSource(lead.getSource());
            dto.setStatus(lead.getStatus());
            dto.setCreatedAt(lead.getCreated_at());
            return dto;
        }).collect(Collectors.toList());
    }

    
    //update lead
    @Override
    public LeadsDTO updateLead(Integer id, LeadsDTO dto) {
        Optional<Leads> optional = leadsRepository.findById(id);
        if (optional.isPresent()) {
            Leads existing = optional.get();
            existing.setFull_name(dto.getFull_name());
            existing.setEmail(dto.getEmail());
            existing.setPhone(dto.getPhone());
            existing.setSource(dto.getSource());
            existing.setStatus(dto.getStatus());
            Leads updated = leadsRepository.save(existing);
            return dto;
        }
        return null;
        }
 

        //delete lead
        @Override
        public void deleteLead(Integer id) {
        leadsRepository.deleteById(id);
        }
    }