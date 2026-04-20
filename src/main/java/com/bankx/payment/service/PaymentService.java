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

/**
 * Service that provides payment functionality against an existing Loan.
 */
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

    /**
     * Makes a payment against a {@link LoanEntity}.
     * The @Transactional annotation ensures that all or no database updates are made.
     *
     * @param payment to be made
     * @throws PaymentAmountIncorrectException when the paymentAmount is invalid
     * @throws LoanNotFoundException when the loanId isn't found
     */
    @Transactional
    public void makePayment(Payment payment)
            throws PaymentAmountIncorrectException, LoanNotFoundException {

        var loan = loanService.retrieveAsEntity(payment.getLoanId());

        validatePaymentAmount(loan, payment);
        updateLoanWithPayment(loan, payment);

        loanRepository.save(loan);
        paymentRepository.save(PaymentEntity.fromPayment(payment));
    }

    /**
     * Updates a {@link LoanEntity} with the given {@link Payment}.
     * Loan is set to SETTLED status when the remaining balance reaches Zero.
     *
     * @param loan to be updated
     * @param payment information to update loan with
     */
    private void updateLoanWithPayment(LoanEntity loan, Payment payment) {
        loan.setRemainingBalance(
                loan.getRemainingBalance().subtract(payment.getPaymentAmount())
        );

        if(loan.getRemainingBalance().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanEntity.LoanStatus.SETTLED);
        }
    }

    /**
     * Validates all aspects of the payment amount.
     *
     * @param loan affected by payment
     * @param payment to be made against loan
     * @throws PaymentAmountIncorrectException when the payment amount is invalid
     */
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
