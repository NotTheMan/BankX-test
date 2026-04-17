package com.bankx.loan.service.exception;

/**
 * Exception is thrown when a {@link com.bankx.loan.controller.model.CreateLoan} to be
 * created is not valid.
 */
public class CreateLoanNotValidException extends Exception {
    public CreateLoanNotValidException(String message) {
        super(message);
    }
}
