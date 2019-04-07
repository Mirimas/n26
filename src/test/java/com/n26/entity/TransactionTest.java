package com.n26.entity;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TransactionTest {

    @Test
    public void createDefault() {
        Transaction transaction = new Transaction();
        assertNotNull(transaction.getId());
    }

}