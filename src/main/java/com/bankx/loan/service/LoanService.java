package com.bankx.loan.service;

import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.repository.LoanRepository;
import com.bankx.loan.repository.entity.LoanEntity;
import com.bankx.loan.service.exception.LoanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan create(CreateLoan createLoan){
        LoanEntity loanEntity = LoanEntity.fromCreateLoan(createLoan);
        loanEntity = loanRepository.save(loanEntity);

        return loanEntity.toLoan();
    }

    public Loan retrieve(UUID loanId) throws LoanNotFoundException {
        return retrieveAsEntity(loanId).toLoan();
    }

    public LoanEntity retrieveAsEntity(UUID loanId) throws LoanNotFoundException {
        return loanRepository
                    .findById(loanId)
                    .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
    }
}
