package com.imperacred.BankLoanApplication.controller;


import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.service.LeadsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeadsController {

    @Autowired
    private LeadsService leadsService;
   
    @PostMapping("/leads")
    public LeadsDTO createLead(@RequestBody LeadsDTO leadsDto) {
        return leadsService.saveLead(leadsDto);
   }
    
    
    @GetMapping("/leads")
    public List<LeadsDTO> getAllLeads() {
        return leadsService.getAllLeads();
    }
    
    @GetMapping("/leads/{id}")
    public LeadsDTO getLeadById(@PathVariable Integer id) {
        return leadsService.getLeadById(id);
    }

    @PutMapping("/leads/{id}")
    public LeadsDTO updateLead(@PathVariable Integer id, @RequestBody LeadsDTO dto) {
      return leadsService.updateLead(id, dto);
    }

    @DeleteMapping("/leads/{id}")
    public String deleteLead(@PathVariable Integer id) {
        leadsService.deleteLead(id);
        return "Lead deleted successfully!";
    }
}

