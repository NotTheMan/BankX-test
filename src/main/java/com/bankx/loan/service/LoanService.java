package com.bankx.loan.service;

import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan create(){
        return new Loan();
    }

    public Loan retrieve(){
        return new Loan();
    }
}
