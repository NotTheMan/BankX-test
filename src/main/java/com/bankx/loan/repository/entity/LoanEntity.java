package com.bankx.loan.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID loanId;

    private BigDecimal loanAmount;

    /**
     * Maximum 2730 years, which seems like enough range for a loan term.
     */
    private short term;

    private LoanStatus status;

    /**
     * Encapsulated within {@link LoanEntity} as it is only used within a Loan.
     * Trying to adhere to single responsibility theory.
     */
    public enum LoanStatus {
        ACTIVE, SETTLED
    }
}
