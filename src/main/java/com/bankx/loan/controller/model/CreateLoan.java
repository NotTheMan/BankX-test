package com.bankx.loan.controller.model;

import com.bankx.loan.repository.entity.LoanEntity.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CreateLoan {
    BigDecimal loanAmount;
    short term;
}
