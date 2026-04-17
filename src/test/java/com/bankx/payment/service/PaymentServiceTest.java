package com.bankx.payment.service;

import org.junit.jupiter.api.Test;

public class PaymentServiceTest {

    @Test
    void makePayment_makesPaymentAgainstCorrectLoan(){

    }

    @Test
    void makePayment_throwsLoanNotFound_whenLoanDoesNotExist(){

    }

    @Test
    void makePayment_throwsPaymentAmountIncorrectException_whenPaymentAmountIsMoreThanRemainingBalance(){

    }

    @Test
    void makePayment_throwsPaymentAmountIncorrectException_whenPaymentAmountIsZero(){

    }

    @Test
    void makePayment_updatesStatusToSettled_whenRemainingBalanceReachesZero(){

    }

    @Test
    void makePayment_savesUpdatedLoan(){

    }

    @Test
    void makePayment_savesPayment(){

    }
}
