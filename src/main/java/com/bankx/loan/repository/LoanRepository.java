package com.bankx.loan.repository;

import com.bankx.loan.repository.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

}
