package com.bankx.loan.controller.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CreateLoanTest {

    @Test
    void isAmountValid_returnsTrue_whenLoanAmountIsGreaterThanZero(){
        var createLoan = new CreateLoan(BigDecimal.ONE, (short) 1);

        assertTrue(createLoan.isAmountValid());
    }

    @Test
    void isAmountValid_returnsFalse_whenLoanAmountIsNull(){
        var createLoan = new CreateLoan(null, (short) 1);

        assertFalse(createLoan.isAmountValid());
    }

    @Test
    void isAmountValid_returnsFalse_whenLoanAmountIsZero(){
        var createLoan = new CreateLoan(BigDecimal.ZERO, (short) 1);

        assertFalse(createLoan.isAmountValid());
    }

    @Test
    void isAmountValid_returnsFalse_whenLoanAmountIsLessThanZero(){
        var createLoan = new CreateLoan(new BigDecimal("-10"), (short) 1);

        assertFalse(createLoan.isAmountValid());
    }

    @Test
    void isTermValid_returnsTrue_whenTermIsGreaterThanZero(){
        var createLoan = new CreateLoan(BigDecimal.ONE, (short) 1);

        assertTrue(createLoan.isTermValid());
    }

    @Test
    void isTermValid_returnsFalse_whenTermIsNull(){
        var createLoan = new CreateLoan(BigDecimal.ONE, null);

        assertFalse(createLoan.isTermValid());
    }

    @Test
    void isTermValid_returnsFalse_whenTermIsZero(){
        var createLoan = new CreateLoan(BigDecimal.ONE, (short) 0);

        assertFalse(createLoan.isTermValid());
    }

    @Test
    void isTermValid_returnsFalse_whenTermIsLessThanZero(){
        var createLoan = new CreateLoan(BigDecimal.ONE, Short.valueOf("-1"));

        assertFalse(createLoan.isTermValid());
    }
}
