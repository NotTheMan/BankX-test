package com.bankx.loan.controller;

import com.bankx.LoanApplication;
import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.service.LoanService;
import com.bankx.loan.service.exception.LoanNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            var loan = loanService.retrieve(loanId);
            return ResponseEntity.ok(loan);
        } catch (LoanNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody CreateLoan loanToCreate) {
        // Input validation is done here, as it's a concern of the rest endpoint,
        // not of the service, in my opinion.
        validate(loanToCreate);

        Loan createdLoan = loanService.create(loanToCreate);
        return ResponseEntity.ok(createdLoan);
    }

    private void validate(CreateLoan createLoan) {
        if(!createLoan.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
