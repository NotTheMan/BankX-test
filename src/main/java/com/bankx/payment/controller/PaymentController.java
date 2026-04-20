package com.bankx.payment.controller;

import com.bankx.loan.service.exception.LoanNotFoundException;
import com.bankx.payment.controller.model.Payment;
import com.bankx.payment.service.PaymentService;
import com.bankx.payment.service.exception.PaymentAmountIncorrectException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * HTTP endpoint that handles
 * 1. Making a payment against a specific loanId.
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * HTTP endpoint that creates a new {@link Payment} against a specific loanId.
     * Checked exceptions are used to indicate business logic failures.
     * These checked exceptions are then translated into response codes.
     *
     * @param paymentToBeMade that describes the payment to be made
     */
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment successfully made against loanId",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request is not well formed or business logic validation failed",
                    content = @Content(schema = @Schema())
            )
    })
    @PostMapping
    public ResponseEntity<Void> makePayment(@RequestBody Payment paymentToBeMade) {
        try {
            paymentService.makePayment(paymentToBeMade);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (PaymentAmountIncorrectException | LoanNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
