package com.bankx.loan.service.exception;

/**
 * This checked exception is thrown to indicate a {@link com.bankx.loan.repository.entity.LoanEntity} does not exist.
 */
public class LoanNotFoundException extends Exception {
    public LoanNotFoundException(String message) {
        super(message);
    }
}
