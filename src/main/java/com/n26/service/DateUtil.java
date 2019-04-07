package com.n26.service;

import com.n26.entity.Transaction;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Utility class for work with data
 */
@Component
public class DateUtil {
    public static final int SECONDS_TRANSACTION_BECOME_OLD = 60;

    /**
     * Checking is transaction timestamp more then one minute
     *
     * @param transaction Transaction
     * @return true if the transaction's timestamp is older then {@link #SECONDS_TRANSACTION_BECOME_OLD} seconds
     */
    public boolean isTransactionOld(Transaction transaction) {
        return Instant.now().minusSeconds(SECONDS_TRANSACTION_BECOME_OLD).isAfter(transaction.getTimestamp().toInstant());
    }

    /**
     * Checking is transaction timestamp created in future
     *
     * @param transaction Transaction
     * @return true if the transaction's timestamp is from future
     */
    public boolean isTransactionInFuture(Transaction transaction) {
        return Instant.now().isBefore(transaction.getTimestamp().toInstant());
    }
}
