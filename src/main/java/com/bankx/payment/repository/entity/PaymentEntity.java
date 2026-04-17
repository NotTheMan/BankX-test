package com.bankx.payment.repository.entity;

import com.bankx.payment.controller.model.Payment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a Payment that resides in persistent storage.
 */
@Entity
@Data
@NoArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;
    private UUID loanId;
    private BigDecimal paymentAmount;

    /**
     * Converts a {@link Payment} to a {@link PaymentEntity}.
     *
     * @param payment to convert
     * @return correspondingPaymentEntity
     */
    public static PaymentEntity fromPayment(Payment payment) {
        var paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentAmount(payment.getPaymentAmount());
        paymentEntity.setLoanId(payment.getLoanId());

        return paymentEntity;
    }
}
