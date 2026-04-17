package com.bankx.loan.repository;

import com.bankx.loan.repository.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Provides JPA support for a {@link LoanEntity}.
 */
@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

}
