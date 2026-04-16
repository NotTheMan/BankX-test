package com.bankx.loan.controller.model;

import lombok.*;
import com.bankx.loan.repository.entity.LoanEntity.LoanStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Loan {
    UUID loanId;
    BigDecimal loanAmount;
    BigDecimal remainingBalance;
    short term;
    LoanStatus status;
}
