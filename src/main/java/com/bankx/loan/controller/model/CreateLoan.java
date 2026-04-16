package com.bankx.loan.controller.model;

import com.bankx.loan.repository.entity.LoanEntity.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CreateLoan {
    BigDecimal loanAmount;
    Short term;

    /**
     * {@link CreateLoan} should know itself if valid.
     * More single responsibility theory.
     *
     * @return isValid
     */
    public boolean isValid(){
        return loanAmount != null && term != null;
    }
}
