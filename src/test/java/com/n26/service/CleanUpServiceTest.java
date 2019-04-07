package com.n26.service;

import com.n26.entity.Transaction;
import com.n26.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanUpServiceTest {
    private CleanUpService cleanUpService;
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
        cleanUpService = new CleanUpService(transactionRepository, new DateUtil());
    }

    @Test
    public void deleteOldData() {
        Transaction transaction = new Transaction();
        UUID id = transaction.getId();
        transaction.setAmount(BigDecimal.ONE);
        transaction.setTimestamp(Date.from(Instant.now().minusSeconds(61)));
        transactionRepository.save(transaction);

        assertTrue(transactionRepository.findById(id).isPresent());

        cleanUpService.deleteOldData();

        assertFalse("Old transaction must be delete", transactionRepository.findById(id).isPresent());
    }
}