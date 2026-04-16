package com.bankx.payment.service.exception;

public class PaymentAmountIncorrectException extends Exception {
    public PaymentAmountIncorrectException(String message) {
        super(message);
    }
}
