package com.bankx.payment.service;

import com.bankx.loan.repository.LoanRepository;
import com.bankx.loan.repository.entity.LoanEntity;
import com.bankx.loan.service.LoanService;
import com.bankx.loan.service.exception.LoanNotFoundException;
import com.bankx.payment.controller.model.Payment;
import com.bankx.payment.repository.PaymentRepository;
import com.bankx.payment.repository.entity.PaymentEntity;
import com.bankx.payment.service.exception.PaymentAmountIncorrectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    private static final UUID LOAN_ID = UUID.randomUUID();

    @Mock
    private PaymentRepository mockPaymentRepository;
    @Mock
    private LoanService mockLoanService;
    @Mock
    private LoanRepository mockLoanRepository;

    private final LoanEntity loanEntityStub = new LoanEntity(
            LOAN_ID, BigDecimal.TEN, BigDecimal.TEN, (short) 1, LoanEntity.LoanStatus.ACTIVE
    );

    private PaymentService paymentService;

    @BeforeEach
    public void setup() throws LoanNotFoundException{
        when(mockLoanService.retrieveAsEntity(eq(LOAN_ID)))
                .thenReturn(loanEntityStub);

        paymentService = new PaymentService(
                mockPaymentRepository, mockLoanRepository, mockLoanService
        );
    }

    @Test
    void makePayment_makesPaymentAgainstCorrectLoan()
            throws LoanNotFoundException, PaymentAmountIncorrectException {

        var payment = new Payment(LOAN_ID, BigDecimal.TEN);
        paymentService.makePayment(payment);

        verify(mockLoanService).retrieveAsEntity(payment.getLoanId());
        verify(mockLoanRepository).save(loanEntityStub);
    }

    @Test
    void makePayment_throwsLoanNotFound_whenLoanDoesNotExist() throws LoanNotFoundException {
        when(mockLoanService.retrieveAsEntity(LOAN_ID))
                .thenThrow(LoanNotFoundException.class);

        var payment = new Payment(LOAN_ID, BigDecimal.TEN);

        Assertions.assertThrows(
                LoanNotFoundException.class,
                () -> paymentService.makePayment(payment)
        );
    }

    @Test
    void makePayment_throwsPaymentAmountIncorrectException_whenPaymentAmountIsMoreThanRemainingBalance(){
        var payment = new Payment(
                LOAN_ID,
                loanEntityStub.getRemainingBalance().add(BigDecimal.ONE)
        );

        Assertions.assertThrows(
                PaymentAmountIncorrectException.class,
                () -> paymentService.makePayment(payment)
        );
    }

    @Test
    void makePayment_throwsPaymentAmountIncorrectException_whenPaymentAmountIsZero(){
        var payment = new Payment(LOAN_ID, BigDecimal.ZERO);

        Assertions.assertThrows(
                PaymentAmountIncorrectException.class,
                () -> paymentService.makePayment(payment)
        );
    }

    @Test
    void makePayment_updatesStatusToSettled_whenRemainingBalanceReachesZero()
            throws LoanNotFoundException, PaymentAmountIncorrectException {

        var payment = new Payment(LOAN_ID, loanEntityStub.getRemainingBalance());
        paymentService.makePayment(payment);

        var expectedUpdatedLoanEntity = new LoanEntity(
                loanEntityStub.getLoanId(),
                loanEntityStub.getLoanAmount(),
                BigDecimal.ZERO,
                loanEntityStub.getTerm(),
                LoanEntity.LoanStatus.SETTLED
        );

        verify(mockLoanRepository).save(eq(expectedUpdatedLoanEntity));
    }

    @Test
    void makePayment_savesUpdatedLoan() throws LoanNotFoundException, PaymentAmountIncorrectException {
        var payment = new Payment(LOAN_ID, BigDecimal.ONE);
        paymentService.makePayment(payment);

        var expectedUpdatedLoanEntity = new LoanEntity(
                loanEntityStub.getLoanId(),
                loanEntityStub.getLoanAmount(),
                loanEntityStub.getLoanAmount().subtract(payment.getPaymentAmount()),
                loanEntityStub.getTerm(),
                LoanEntity.LoanStatus.ACTIVE
        );

        verify(mockLoanRepository).save(eq(expectedUpdatedLoanEntity));
    }

    @Test
    void makePayment_savesPayment() throws LoanNotFoundException, PaymentAmountIncorrectException {
        var payment = new Payment(LOAN_ID, BigDecimal.TEN);
        var paymentEntity = PaymentEntity.fromPayment(payment);

        paymentService.makePayment(payment);

        verify(mockPaymentRepository).save(paymentEntity);
    }
}
