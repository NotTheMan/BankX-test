package com.bankx.loan.controller;

import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.service.LoanService;
import com.bankx.loan.service.exception.CreateLoanNotValidException;
import com.bankx.loan.service.exception.LoanNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {

    @Mock
    private LoanService mockLoanService;

    private LoanController loanController;

    @BeforeEach
    public void setup(){
        loanController = new LoanController(mockLoanService);
    }

    @Test
    void createLoan_returnsCreatedHttpCodeWithLoan_whenLoanIsCreatedSuccessfully() throws CreateLoanNotValidException {
        when(mockLoanService.create(any())).thenReturn(new Loan());

        var createLoan = new CreateLoan(BigDecimal.TEN, (short) 1);
        ResponseEntity<Loan> response = loanController.createLoan(createLoan);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createLoan_returnsBadRequest_whenCreateLoanIsNotValid() throws CreateLoanNotValidException {
        when(mockLoanService.create(any()))
                .thenThrow(CreateLoanNotValidException.class);

        var createLoan = new CreateLoan(BigDecimal.ZERO, (short) 1);

        assertThrows(
                ResponseStatusException.class,
                () -> loanController.createLoan(createLoan)
        );

        try {
            loanController.createLoan(createLoan);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void getLoadById_returnsOkHttpCodeWithLoan_whenLoanIsRetrievedSuccessfully() throws LoanNotFoundException {
        when(mockLoanService.retrieve(any())).thenReturn(new Loan());

        ResponseEntity<Loan> response = loanController.getLoadById(UUID.randomUUID());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getLoadById_returnsNotFoundHttpCode_whenLoanIdCannotBeFound() throws LoanNotFoundException {
        when(mockLoanService.retrieve(any()))
                .thenThrow(LoanNotFoundException.class);

        assertThrows(
                ResponseStatusException.class,
                () -> loanController.getLoadById(UUID.randomUUID())
        );

        try {
            loanController.getLoadById(UUID.randomUUID());
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}
