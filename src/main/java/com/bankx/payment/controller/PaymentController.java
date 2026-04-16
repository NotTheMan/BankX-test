package com.bankx.payment.controller;

import com.bankx.loan.service.exception.LoanNotFoundException;
import com.bankx.payment.controller.model.Payment;
import com.bankx.payment.service.PaymentService;
import com.bankx.payment.service.exception.PaymentAmountIncorrectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> makePayment(@RequestBody Payment paymentToBeMade) {
        try {
            paymentService.makePayment(paymentToBeMade);
            return ResponseEntity.ok().build();
        } catch (PaymentAmountIncorrectException | LoanNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
