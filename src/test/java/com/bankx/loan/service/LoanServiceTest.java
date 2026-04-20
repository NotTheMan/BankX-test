package com.bankx.loan.service;

import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.repository.LoanRepository;
import com.bankx.loan.repository.entity.LoanEntity;
import com.bankx.loan.service.exception.CreateLoanNotValidException;
import com.bankx.loan.service.exception.LoanNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
public class LoanServiceTest {

    @Mock
    private LoanRepository mockLoanRepository;
    private LoanService loanService;

    @BeforeEach
    public void setup(){
        lenient().when(mockLoanRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        loanService = new LoanService(mockLoanRepository);
    }

    @Test
    void create_throwsCreateLoanNotValidException_whenLoanAmountIsNotSet() {
        var createLoan = new CreateLoan(null, (short) 1);

        assertThrows(
                CreateLoanNotValidException.class,
                () -> loanService.create(createLoan)
        );
    }

    @Test
    void create_throwsCreateLoanNotValidException_whenLoanAmountIsZero() {
        var createLoan = new CreateLoan(BigDecimal.ZERO, (short) 1);

        assertThrows(
                CreateLoanNotValidException.class,
                () -> loanService.create(createLoan)
        );
    }

    @Test
    void create_throwsCreateLoanNotValidException_whenLoanTermIsNotSet() {
        var createLoan = new CreateLoan(BigDecimal.TEN, null);

        assertThrows(
                CreateLoanNotValidException.class,
                () -> loanService.create(createLoan)
        );
    }

    @Test
    void create_throwsCreateLoanNotValidException_whenLoanTermIsZero() {
        var createLoan = new CreateLoan(BigDecimal.TEN, (short) 0);

        assertThrows(
                CreateLoanNotValidException.class,
                () -> loanService.create(createLoan)
        );
    }

    @Test
    void create_usesLoanRepository_toSaveValidLoan() throws CreateLoanNotValidException {
        var createLoan = new CreateLoan(BigDecimal.TEN, (short) 1);
        var expectedLoanEntity = LoanEntity.fromCreateLoan(createLoan);

        loanService.create(createLoan);

        verify(mockLoanRepository).save(ArgumentMatchers.eq(expectedLoanEntity));
    }

    @Test
    void retrieve_throwsLoanNotFoundException_whenLoanCannotBeFound() {
        assertThrows(
                LoanNotFoundException.class,
                () -> loanService.retrieve(UUID.randomUUID())
        );
    }

    @Test
    void retrieve_returnsLoanEntityFoundByRepository() throws LoanNotFoundException {
        var expectedLoanEntity = new LoanEntity(
                UUID.randomUUID(),
                BigDecimal.TEN,
                BigDecimal.TEN,
                (short) 1,
                LoanEntity.LoanStatus.ACTIVE
        );

        when(mockLoanRepository.findById(expectedLoanEntity.getLoanId()))
                .thenReturn(Optional.of(expectedLoanEntity));

        var retrievedLoan = loanService.retrieve(expectedLoanEntity.getLoanId());

        assertEquals(expectedLoanEntity.toLoan(), retrievedLoan);
    }
}
