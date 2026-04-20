package com.bankx.loan.service;

import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.repository.LoanRepository;
import com.bankx.loan.repository.entity.LoanEntity;
import com.bankx.loan.service.exception.CreateLoanNotValidException;
import com.bankx.loan.service.exception.LoanNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service that provides functionality to create and retrieve a {@link Loan}.
 */
@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    /**
     * Creates a new {@link Loan} from a {@link CreateLoan} in persistent storage.
     *
     * @param createLoan to create
     * @return createdLoan that was created in persistent storage
     * @throws CreateLoanNotValidException when {@link CreateLoan} is invalid
     */
    public Loan create(CreateLoan createLoan) throws CreateLoanNotValidException {
        validate(createLoan);

        LoanEntity loanEntity = LoanEntity.fromCreateLoan(createLoan);
        loanEntity = loanRepository.save(loanEntity);

        return loanEntity.toLoan();
    }

    /**
     * Retrieves a {@link Loan} from persistent storage.
     *
     * @param loanId to retrieve
     * @return retrievedLoan
     * @throws LoanNotFoundException when the loan described by loanId does not exist.
     */
    public Loan retrieve(UUID loanId) throws LoanNotFoundException {
        return retrieveAsEntity(loanId).toLoan();
    }

    /**
     * Convenience method to retrieve a loan as a {@link LoanEntity}.
     * This method can be used by other service classes, to ensure business logic remains intact when retrieving a loan.
     *
     * @param loanId to retrieve
     * @return retrievedLoan
     * @throws LoanNotFoundException when the loan described by loanId does not exist.
     */
    public LoanEntity retrieveAsEntity(UUID loanId) throws LoanNotFoundException {
        return loanRepository
                    .findById(loanId)
                    .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
    }

    /**
     * Validates all aspects of a {@link CreateLoan}.
     * @param createLoan to be verified
     * @throws CreateLoanNotValidException when {@link CreateLoan} is not valid, with failure reason.
     */
    private void validate(CreateLoan createLoan) throws CreateLoanNotValidException {
        if(!createLoan.isAmountValid()) {
            throw new CreateLoanNotValidException("Loan amount is not valid");
        } else if(!createLoan.isTermValid()) {
            throw new CreateLoanNotValidException("Loan term is not valid");
        }
    }
}
