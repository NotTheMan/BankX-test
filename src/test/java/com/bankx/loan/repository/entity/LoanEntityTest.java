package com.bankx.loan.repository.entity;

import com.bankx.loan.controller.model.CreateLoan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class LoanEntityTest {

    @Test
    void fromCreateLoan_doesNotSetLoanId() {
        var createLoan = new CreateLoan(BigDecimal.TEN, (short) 1);
        var loanEntity = LoanEntity.fromCreateLoan(createLoan);

        Assertions.assertNull(loanEntity.getLoanId());
    }

    @Test
    void fromCreateLoan_addsNecessaryLoanEntityFields() {
        var loanAmount = new BigDecimal("10");
        var loanTerm = (short) 10;
        var createLoan = new CreateLoan(loanAmount, loanTerm);

        var loanEntity = LoanEntity.fromCreateLoan(createLoan);

        Assertions.assertEquals(loanAmount, loanEntity.getLoanAmount());
        Assertions.assertEquals(loanAmount, loanEntity.getRemainingBalance());
        Assertions.assertEquals(loanTerm, loanEntity.getTerm());
        Assertions.assertEquals(LoanEntity.LoanStatus.ACTIVE, loanEntity.getStatus());
    }

    @Test
    void toLoan_populatesCorrectly(){
        var loanEntity = LoanEntity.fromCreateLoan(new CreateLoan(BigDecimal.TEN, (short) 1));
        var loan = loanEntity.toLoan();

        Assertions.assertEquals(loanEntity.getLoanId(), loan.getLoanId());
        Assertions.assertEquals(loanEntity.getLoanAmount(), loan.getLoanAmount());
        Assertions.assertEquals(loanEntity.getRemainingBalance(), loan.getRemainingBalance());
        Assertions.assertEquals(loanEntity.getTerm(), loan.getTerm());
        Assertions.assertEquals(loanEntity.getStatus(), loan.getStatus());
    }
}
