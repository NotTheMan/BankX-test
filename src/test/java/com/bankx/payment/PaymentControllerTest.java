package com.bankx.payment;

import com.bankx.loan.service.exception.LoanNotFoundException;
import com.bankx.payment.controller.PaymentController;
import com.bankx.payment.controller.model.Payment;
import com.bankx.payment.service.PaymentService;
import com.bankx.payment.service.exception.PaymentAmountIncorrectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService mockPaymentService;

    private PaymentController paymentController;

    @BeforeEach
    public void setup(){
        paymentController = new PaymentController(mockPaymentService);
    }

    @Test
    void returnsBadRequestHttpCode_whenPaymentAmountIncorrectExceptionIsThrown()
            throws LoanNotFoundException, PaymentAmountIncorrectException {

        doThrow(PaymentAmountIncorrectException.class)
                .when(mockPaymentService).makePayment(any());

        var payment = new Payment(UUID.randomUUID(), new BigDecimal("-1"));

        assertThrows(
                ResponseStatusException.class,
                () -> paymentController.makePayment(payment)
        );

        try {
            paymentController.makePayment(payment);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void returnsBadRequestHttpCode_whenLoanNotFoundExceptionIsThrown()
            throws LoanNotFoundException, PaymentAmountIncorrectException {

        doThrow(LoanNotFoundException.class)
                .when(mockPaymentService).makePayment(any());

        var payment = new Payment(UUID.randomUUID(), BigDecimal.TEN);

        assertThrows(
                ResponseStatusException.class,
                () -> paymentController.makePayment(payment)
        );

        try {
            paymentController.makePayment(payment);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void returnsCreatedHttpCode_whenPaymentIsSuccessful(){
        var payment = new Payment(UUID.randomUUID(), BigDecimal.TEN);

        ResponseEntity<Void> response = paymentController.makePayment(payment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
