package com.bankx.payment.repository.entity;

import com.bankx.payment.controller.model.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentEntityTest {

    @Test
    void fromPayment_populatesAllPaymentEntityFields(){
        var payment = new Payment(UUID.randomUUID(), BigDecimal.TEN);
        var paymentEntity = PaymentEntity.fromPayment(payment);

        Assertions.assertEquals(payment.getLoanId(), paymentEntity.getLoanId());
        Assertions.assertEquals(payment.getPaymentAmount(), paymentEntity.getPaymentAmount());
        Assertions.assertNull(paymentEntity.getPaymentId());
    }
}
