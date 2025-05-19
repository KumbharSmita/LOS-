package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.SanctionLetterDTO;
import com.imperacred.BankLoanApplication.service.SanctionLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sanction-letters")
public class SanctionLetterController {

    @Autowired
    private SanctionLetterService service;

    @PostMapping
    public ResponseEntity<SanctionLetterDTO> create(@RequestBody SanctionLetterDTO dto) {
        return ResponseEntity.ok(service.createSanctionLetter(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanctionLetterDTO> getById(@PathVariable Integer id) {
        SanctionLetterDTO dto = service.getSanctionLetterById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<SanctionLetterDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSanctionLetters());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.deleteSanctionLetter(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}