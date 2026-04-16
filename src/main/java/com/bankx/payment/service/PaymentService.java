package com.bankx.payment.service;

import com.bankx.loan.repository.LoanRepository;
import com.bankx.loan.repository.entity.LoanEntity;
import com.bankx.loan.service.LoanService;
import com.bankx.loan.service.exception.LoanNotFoundException;
import com.bankx.payment.controller.model.Payment;
import com.bankx.payment.repository.PaymentRepository;
import com.bankx.payment.repository.entity.PaymentEntity;
import com.bankx.payment.service.exception.PaymentAmountIncorrectException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;
    private final LoanService loanService;

    public PaymentService(PaymentRepository paymentRepository,
                          LoanRepository loanRepository,
                          LoanService loanService) {
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
        this.loanService = loanService;
    }

    @Transactional
    public void makePayment(Payment payment)
            throws PaymentAmountIncorrectException, LoanNotFoundException {

        var loan = loanService.retrieveAsEntity(payment.getLoanId());

        validatePaymentAmount(loan, payment);
        updateLoanWithPayment(loan, payment);

        loanRepository.save(loan);
        paymentRepository.save(PaymentEntity.fromPayment(payment));
    }

    private void updateLoanWithPayment(LoanEntity loan, Payment payment) {
        loan.setRemainingBalance(
                loan.getRemainingBalance().subtract(payment.getPaymentAmount())
        );

        if(loan.getRemainingBalance().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanEntity.LoanStatus.SETTLED);
        }
    }

    private void validatePaymentAmount(LoanEntity loan, Payment payment)
            throws PaymentAmountIncorrectException {

        if(loan.getRemainingBalance().compareTo(payment.getPaymentAmount()) < 0){
            throw new PaymentAmountIncorrectException(
                    "Payment amount is more than remaining balance."
            );
        } else if(payment.getPaymentAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new PaymentAmountIncorrectException(
                    "Payment amount cannot zero."
            );
        }
    }
}
