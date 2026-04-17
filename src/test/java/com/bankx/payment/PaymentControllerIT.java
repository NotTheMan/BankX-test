package com.bankx.payment;

import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.repository.entity.LoanEntity;
import com.bankx.payment.controller.model.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerIT {
    @LocalServerPort
    private int port;

    private RestTestClient client;

    @BeforeEach
    public void setup() {
        client = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    /**
     * Tests making a {@link Payment} against a created {@link Loan}.
     */
    @Test
    void createLoanThenRetrieve() {
        var loanAmount = new BigDecimal(10).setScale(2, RoundingMode.UNNECESSARY);
        var paymentAmount = new BigDecimal(10).setScale(2, RoundingMode.UNNECESSARY);
        var expectedRemainingBalanceAfterPayment = new BigDecimal(0).setScale(2, RoundingMode.UNNECESSARY);
        var expectedStatus = LoanEntity.LoanStatus.SETTLED;
        var createLoan = new CreateLoan(loanAmount, (short) 1);

        Loan createdLoan = createLoan(createLoan);
        // Assert created Loan, to aid in finding problems down the line
        assertCreatedLoanIsCorrect(createdLoan, loanAmount);

        var payment = new Payment(createdLoan.getLoanId(), paymentAmount);
        makePayment(payment);

        Loan retrievedLoan = retrieveLoan(createdLoan.getLoanId());

        // Assert that retrieved loan reflects the payment
        Assertions.assertEquals(expectedRemainingBalanceAfterPayment, retrievedLoan.getRemainingBalance());
        Assertions.assertEquals(expectedStatus, retrievedLoan.getStatus());
    }

    private static void assertCreatedLoanIsCorrect(Loan createdLoan, BigDecimal loanAmount) {
        Assertions.assertNotNull(createdLoan);
        Assertions.assertEquals(loanAmount, createdLoan.getLoanAmount());
        Assertions.assertEquals((short) 1, createdLoan.getTerm());
        Assertions.assertEquals(LoanEntity.LoanStatus.ACTIVE, createdLoan.getStatus());
        Assertions.assertEquals(createdLoan.getLoanAmount(), createdLoan.getRemainingBalance());
    }

    private void makePayment(Payment payment) {
        client.post()
            .uri("payments")
            .body(payment)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Void.class)
            .returnResult();
    }

    private Loan createLoan(CreateLoan createLoan) {
        return client.post()
                .uri("loans")
                .body(createLoan)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Loan.class)
                .returnResult().getResponseBody();
    }

    private Loan retrieveLoan(UUID loanId) {
        return client.get()
                .uri("loans/" + loanId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Loan.class)
                .returnResult().getResponseBody();
    }
}
