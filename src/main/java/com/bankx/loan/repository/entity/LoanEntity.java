package com.bankx.loan.repository.entity;

import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID loanId;

    private BigDecimal loanAmount;

    private BigDecimal remainingBalance;

    /**
     * Maximum 2730 years, which seems like enough range for a loan term.
     */
    private short term;

    private LoanStatus status;

    public Loan toLoan() {
        return Loan.builder()
                .loanId(loanId)
                .loanAmount(loanAmount)
                .term(term)
                .remainingBalance(remainingBalance)
                .status(status)
                .build();
    }

    public static LoanEntity fromCreateLoan(CreateLoan createLoan) {
        return new LoanEntity(
                null,
                createLoan.getLoanAmount(),
                createLoan.getLoanAmount(), // remaining balance equals loan amount at start
                createLoan.getTerm(),
                LoanStatus.ACTIVE
        );
    }

    /**
     * Encapsulated within {@link LoanEntity} as it is only used within a Loan.
     * Trying to adhere to single responsibility theory.
     */
    public enum LoanStatus {
        ACTIVE, SETTLED
    }
}
