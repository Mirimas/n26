package com.n26.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.entity.Transaction;
import com.n26.exception.TransactionTimestampException;
import com.n26.repository.TransactionRepository;
import com.n26.service.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Endpoints:
 * POST /transactions – called every time a transaction is made. It is also the sole input of this rest API.
 * DELETE /transactions – deletes all transactions.
 */
@RestControllerAdvice
@RequestMapping(path = "/transactions")
@Slf4j
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final DateUtil dateUtil;

    public TransactionController(TransactionRepository transactionRepository, DateUtil dateUtil) {
        this.transactionRepository = transactionRepository;
        this.dateUtil = dateUtil;
    }

    /**
     * POST /transactions
     * This endpoint is called to create a new transaction.
     * <p>
     * Body:
     * {
     * "amount": "12.3343",
     * "timestamp": "2018-07-17T09:59:51.312Z"
     * }
     * <p>
     *
     * @param transaction transaction
     * @return Empty body with one of the following:
     * 201 – in case of success
     * 204 – if the transaction is older than {@link DateUtil#SECONDS_TRANSACTION_BECOME_OLD} seconds
     * 400 – if the JSON is invalid
     * 422 – if any of the fields are not parsable or the transaction date is in the future
     * @throws TransactionTimestampException if transaction date is invalid and handle it at {@link #transactionTimestampException}
     */
    @RequestMapping(method = POST)
    public ResponseEntity createTransaction(@Valid @NotNull @RequestBody Transaction transaction) throws TransactionTimestampException {
        // If the transaction date is in the future then return 422
        if (dateUtil.isTransactionInFuture(transaction)) {
            throw new TransactionTimestampException(UNPROCESSABLE_ENTITY, "Transaction " + transaction + " is in the future");
        }

        // If the transaction is older than {@link DateUtil#SECONDS_TRANSACTION_BECOME_OLD} seconds then return 204
        if (dateUtil.isTransactionOld(transaction)) {
            throw new TransactionTimestampException(NO_CONTENT, "Transaction " + transaction + " is older than "
                    + DateUtil.SECONDS_TRANSACTION_BECOME_OLD + " seconds");
        }

        transactionRepository.save(transaction);
        log.debug("Transaction {} saved", transaction);
        return ResponseEntity.status(CREATED).build();
    }

    /**
     * DELETE /transactions – deletes all transactions.
     * <p>
     * This endpoint causes all existing transactions to be deleted.
     * The endpoint should accept an empty request body and return a 204 status code.
     */
    @RequestMapping(method = DELETE)
    @ResponseStatus(value = NO_CONTENT)
    public void deleteTransactions() {
        transactionRepository.deleteAll();
        log.debug("Existing transactions are deleted");
    }

    /**
     * TransactionTimestampException handler.
     *
     * @param exception TransactionTimestampException
     * @return ResponseEntity
     */
    @ExceptionHandler(TransactionTimestampException.class)
    public ResponseEntity transactionTimestampException(TransactionTimestampException exception) {
        log.error("Wrong Transaction timestamp", exception);
        return ResponseEntity.status(exception.getHttpStatus()).build();
    }

    /**
     * Return status code 422 – if any of the fields are not parsable
     *
     * @param exception JSON parse exception
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    public void messageNotReadableException(InvalidFormatException exception) {
        log.error("JSON fields are not parsable", exception);
    }
}
