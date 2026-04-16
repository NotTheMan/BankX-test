package com.bankx.payment.controller;

import com.bankx.payment.controller.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<Void> makePayment(@RequestBody Payment paymentToBeMade) {
        return ResponseEntity.ok().build();
    }
}
