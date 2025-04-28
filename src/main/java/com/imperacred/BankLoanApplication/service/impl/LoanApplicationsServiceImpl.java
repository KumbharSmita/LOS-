package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.LoanApplicationsDTO;
import com.imperacred.BankLoanApplication.model.LoanApplications;
import com.imperacred.BankLoanApplication.repository.LoanApplicationsRepository;
import com.imperacred.BankLoanApplication.service.LoanApplicationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanApplicationsServiceImpl implements LoanApplicationsService {

    private final LoanApplicationsRepository repository;

    private LoanApplicationsDTO mapToDTO(LoanApplications app) {
        return LoanApplicationsDTO.builder()
                .application_id(app.getApplication_id())
                .leads_id(app.getLeads_id())
                .loan_type(app.getLoan_type())
                .amount(app.getAmount())
                .tenure_months(app.getTenure_months())
                .purpose(app.getPurpose())
                .status(app.getStatus())
                .submitted_at(app.getSubmitted_at())
                .build();
    }

    private LoanApplications mapToEntity(LoanApplicationsDTO dto) {
        LoanApplications.LoanApplicationsBuilder builder = LoanApplications.builder()
                .leads_id(dto.getLeads_id())
                .loan_type(dto.getLoan_type())
                .amount(dto.getAmount())
                .tenure_months(dto.getTenure_months())
                .purpose(dto.getPurpose())
                .status(dto.getStatus());

        if (dto.getApplication_id() != null) {
            builder.application_id(dto.getApplication_id());
        }

        return builder.build();
    }

    @Override
    public List<LoanApplicationsDTO> getAllApplications() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LoanApplicationsDTO getApplicationById(Integer id) {
        LoanApplications app = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Application not found with id: " + id));
        return mapToDTO(app);
    }

    @Override
    public LoanApplicationsDTO createApplication(LoanApplicationsDTO dto) {
        LoanApplications entity = mapToEntity(dto);
        entity.setSubmitted_at(LocalDateTime.now()); // Set submitted_at automatically
        return mapToDTO(repository.save(entity));
    }

    @Override
    public LoanApplicationsDTO updateApplication(Integer id, LoanApplicationsDTO dto) {
        LoanApplications existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Application not found with id: " + id));

        existing.setLeads_id(dto.getLeads_id());
        existing.setLoan_type(dto.getLoan_type());
        existing.setAmount(dto.getAmount());
        existing.setTenure_months(dto.getTenure_months());
        existing.setPurpose(dto.getPurpose());
        existing.setStatus(dto.getStatus());
        // Do NOT update submitted_at here

        return mapToDTO(repository.save(existing));
    }

    @Override
    public boolean deleteApplication(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}