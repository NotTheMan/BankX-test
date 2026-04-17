package com.bankx.payment.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Object representing a payment that is being made.
 * <p>
 * The @Data annotation generates getters and setters.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private UUID loanId;
    private BigDecimal paymentAmount;
}
