package com.bankx.loan.controller.model;

import lombok.*;
import com.bankx.loan.repository.entity.LoanEntity.LoanStatus;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response Object that is returned from the {@link com.bankx.loan.controller.LoanController}.
 * <p>
 *  The @Value annotation sets all fields to private scope.
 *  In the spirit of clean code, the private access modifier was therefore ommitted.
 *  No setters are generated with the @Value annotation, which is why the builder pattern is used.
 * <p/>
 * <p>
 *  The @Builder annotation assists with adding a builder pattern to this object.
 * </p>
 *
 */
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
