<<<<<<< HEAD
package com.imperacred.BankLoanApplication.service.impl;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.model.AgentLoads;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.LeadAssignments;
import com.imperacred.BankLoanApplication.repository.AgentLoadsRepository;
import com.imperacred.BankLoanApplication.repository.LeadAssignmentRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;

import jakarta.transaction.Transactional;

@Service
public class LeadAssignmentServiceImpl implements LeadAssignmentService {

    private static final Logger logger = LogManager.getLogger(LeadAssignmentServiceImpl.class);

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
    public LeadAssignmentResponseDTO assignLeadToAgent(Integer leads_id) {
        logger.info("Starting lead assignment for lead ID: {}", leads_id);

        Lead lead = leadsRepo.findById(leads_id)
                .orElseThrow(() -> {
                    logger.error("Lead not found with ID: {}", leads_id);
                    return new RuntimeException("Lead not found");
                });
        logger.debug("Found lead: {}", lead.getFirstName()); 

        AgentLoads agentLoads = agentLoadsRepo.findFirstByOrderByLeadCountAscLastAssignedAsc()
                .orElseThrow(() -> {
                    logger.error("No available agents for lead assignment");
                    return new RuntimeException("No agents available");
                });
        logger.debug("Assigning lead to agent ID: {}", agentLoads.getAgent_id());

        // Update agent load
        agentLoads.setLeadCount(agentLoads.getLeadCount() + 1);
        agentLoads.setLastAssigned(LocalDateTime.now());
        agentLoadsRepo.save(agentLoads);
        logger.info("Updated agent load for agent ID: {}", agentLoads.getAgent_id());

        // Create new lead assignment
        LeadAssignments assignment = new LeadAssignments();
        assignment.setLeads_id(leads_id);
        assignment.setAgent_id(agentLoads.getAgent_id());
        assignment.setAssigned_at(LocalDateTime.now());
        assignment.setStatus("ASSIGNED");
        leadAssignmentRepo.save(assignment);
        logger.info("Lead assigned: Lead ID {} -> Agent ID {}", leads_id, agentLoads.getAgent_id());

        return new LeadAssignmentResponseDTO(
                leads_id,
                agentLoads.getAgent_id(),
                assignment.getAssigned_at(),
                assignment.getStatus()
        );
    }
}
=======
//package com.imperacred.BankLoanApplication.service.impl;
//
//
//
//import java.time.LocalDateTime;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
//import com.imperacred.BankLoanApplication.model.AgentLoads;
//import com.imperacred.BankLoanApplication.model.LeadAssignments;
//import com.imperacred.BankLoanApplication.model.Leads;
//import com.imperacred.BankLoanApplication.repository.AgentLoadsRepository;
//import com.imperacred.BankLoanApplication.repository.LeadAssignmentRepository;
//import com.imperacred.BankLoanApplication.repository.LeadsRepository;
//import com.imperacred.BankLoanApplication.service.LeadAssignmentService;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class LeadAssignmentServiceImpl implements LeadAssignmentService {
//
//   
//    @Autowired
//    private LeadsRepository leadsRepo;
//
//    private final AgentLoadsRepository agentLoadsRepo;
//    private final LeadAssignmentRepository leadAssignmentRepo;
//
//    @Autowired
//    public LeadAssignmentServiceImpl(AgentLoadsRepository agentLoadsRepo, LeadAssignmentRepository leadAssignmentRepo) {
//        this.agentLoadsRepo = agentLoadsRepo;
//        this.leadAssignmentRepo = leadAssignmentRepo;
//    }
//    @Transactional
//    @Override
//    public LeadAssignmentResponseDTO assignLeadToAgent(int leads_id) {
//      
//        Leads lead = leadsRepo.findById(leads_id)
//                .orElseThrow(() -> new RuntimeException("Lead not found"));
//
//        
//        AgentLoads agentLoads = agentLoadsRepo.findFirstByOrderByLeadCountAscLastAssignedAsc()
//                .orElseThrow(() -> new RuntimeException("No agents available"));
//
//        
//        agentLoads.setLeadCount(agentLoads.getLeadCount() + 1);
//        agentLoads.setLastAssigned(LocalDateTime.now());
//        agentLoadsRepo.save(agentLoads);
//
//
//       
//        LeadAssignments assignment = new LeadAssignments();
//        
//        assignment.setLeads_id(leads_id);
//        assignment.setAgent_id(agentLoads.getAgent_id());
//        assignment.setAssigned_at(LocalDateTime.now());
//        assignment.setStatus("ASSIGNED");
//        leadAssignmentRepo.save(assignment);
//        System.out.println(assignment);
//
//      return new LeadAssignmentResponseDTO(
//        	    leads_id,  
//        	    agentLoads.getAgent_id(),  
//        	    assignment.getAssigned_at(),
//        	    assignment.getStatus()
//        	);
//
//    }
//}
>>>>>>> origin/feature2
