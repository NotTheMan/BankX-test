package com.bankx.loan.controller;

import com.bankx.LoanApplication;
import com.bankx.loan.controller.model.CreateLoan;
import com.bankx.loan.controller.model.Loan;
import com.bankx.loan.service.LoanService;
import com.bankx.loan.service.exception.CreateLoanNotValidException;
import com.bankx.loan.service.exception.LoanNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * HTTP endpoint that handles
 * 1. Creating a Loan.
 * 2. Retrieving an already created Loan.
 */
@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * HTTP endpoint that creates a new {@link Loan}.
     * Input validation is performed to ensure the input object is populated with accetable values.
     *
     * @param loanToCreate detailing the loan to create.
     * @return {@link Loan} that was created
     */
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Loan successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request is not well formed or input validation failed",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<Loan> createLoan(@RequestBody CreateLoan loanToCreate) {
        try {
            Loan createdLoan = loanService.create(loanToCreate);
            return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
        } catch (CreateLoanNotValidException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * HTTP endpoint that retrieves an already created Loan.
     *
     * @param loanId to retrieve
     * @return {@link Loan} that was previously created.
     */
    @GetMapping("/{loanId}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request is not well formed or input validation failed",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Loan described by loanId does not exist",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<Loan> getLoadById(@PathVariable UUID loanId) {
        try {
            var loan = loanService.retrieve(loanId);
            return ResponseEntity.ok(loan);
        } catch (LoanNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
