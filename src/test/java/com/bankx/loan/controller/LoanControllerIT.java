package com.bankx.loan.controller;

import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.repository.entity.LoanEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoanControllerIT {
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
     * Tests creating a {@link Loan} and whether the retrieved loan is equal to the created loan.
     */
    @Test
    void createLoanThenRetrieve() {
        var loanAmount = new BigDecimal(10).setScale(2, RoundingMode.UNNECESSARY);

        CreateLoan createLoan = new CreateLoan();
        createLoan.setLoanAmount(loanAmount);
        createLoan.setTerm((short) 1);

        Loan createdLoan = client.post()
                .uri("loans")
                .body(createLoan)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Loan.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(createdLoan);
        Assertions.assertEquals(loanAmount, createdLoan.getLoanAmount());
        Assertions.assertEquals((short) 1, createdLoan.getTerm());
        Assertions.assertEquals(LoanEntity.LoanStatus.ACTIVE, createdLoan.getStatus());
        Assertions.assertEquals(createdLoan.getLoanAmount(), createdLoan.getRemainingBalance());

        Loan retrievedLoan = client.get()
                .uri("loans/" + createdLoan.getLoanId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Loan.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals(createdLoan, retrievedLoan);
    }
}
