package com.bankx.loan.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.math.BigDecimal;

/**
 * Input Object for the {@link com.bankx.loan.controller.LoanController} in order to create a Loan.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CreateLoan {
    private BigDecimal loanAmount;
    private Short term;

    /**
     * {@link CreateLoan} should know itself if the loanAmount is valid or not.
     * Single responsibility theory is used here.
     *
     * @return isLoanAmountValid
     */
    @JsonIgnore
    public boolean isAmountValid(){
        return
                loanAmount != null &&
                loanAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * {@link CreateLoan} should know itself if the term is valid or not.
     * Single responsibility theory is used here.
     *
     * @return isTermValid
     */
    @JsonIgnore
    public boolean isTermValid(){
        return term != null && term > 0;
    }
}
