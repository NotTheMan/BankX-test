package com.bankx.loan.controller;

import com.bankx.LoanApplication;
import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoadById(@PathVariable UUID loanId) {
        return ResponseEntity.ok(null);
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody CreateLoan loanToCreate) {
        return ResponseEntity.ok(null);
    }
}
