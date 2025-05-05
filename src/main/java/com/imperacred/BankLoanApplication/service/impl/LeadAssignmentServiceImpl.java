package com.imperacred.BankLoanApplication.service.impl;



import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.model.AgentLoads;
import com.imperacred.BankLoanApplication.model.LeadAssignments;
import com.imperacred.BankLoanApplication.model.Leads;
import com.imperacred.BankLoanApplication.repository.AgentLoadsRepository;
import com.imperacred.BankLoanApplication.repository.LeadAssignmentRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;

import jakarta.transaction.Transactional;

@Service
public class LeadAssignmentServiceImpl implements LeadAssignmentService {

   
    @Autowired
    private LeadsRepository leadsRepo;

    private final AgentLoadsRepository agentLoadsRepo;
    private final LeadAssignmentRepository leadAssignmentRepo;

    @Autowired
    public LeadAssignmentServiceImpl(AgentLoadsRepository agentLoadsRepo, LeadAssignmentRepository leadAssignmentRepo) {
        this.agentLoadsRepo = agentLoadsRepo;
        this.leadAssignmentRepo = leadAssignmentRepo;
    }
    @Transactional
    @Override
    public LeadAssignmentResponseDTO assignLeadToAgent(int leads_id) {
      
        Leads lead = leadsRepo.findById(leads_id)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        
        AgentLoads agentLoads = agentLoadsRepo.findFirstByOrderByLeadCountAscLastAssignedAsc()
                .orElseThrow(() -> new RuntimeException("No agents available"));

        
        agentLoads.setLeadCount(agentLoads.getLeadCount() + 1);
        agentLoads.setLastAssigned(LocalDateTime.now());
        agentLoadsRepo.save(agentLoads);


       
        LeadAssignments assignment = new LeadAssignments();
        
        assignment.setLeads_id(leads_id);
        assignment.setAgent_id(agentLoads.getAgent_id());
        assignment.setAssigned_at(LocalDateTime.now());
        assignment.setStatus("ASSIGNED");
        leadAssignmentRepo.save(assignment);
        System.out.println(assignment);

      return new LeadAssignmentResponseDTO(
        	    leads_id,  
        	    agentLoads.getAgent_id(),  
        	    assignment.getAssigned_at(),
        	    assignment.getStatus()
        	);

    }
}
