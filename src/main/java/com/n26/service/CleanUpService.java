package com.n26.service;

import com.n26.entity.Transaction;
import com.n26.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service delete old transactions from the memory.
 * Your service must not store all transactions in memory for all time. Transactions not necessary for correct calculation MUST be discarded.
 */
@Configuration
@EnableScheduling
@Slf4j
public class CleanUpService {
    private static final long FIVE_MINUTES_IN_MILLIS = 5 * 60_000;

    private final TransactionRepository transactionRepository;
    private final DateUtil dateUtil;

    public CleanUpService(TransactionRepository transactionRepository, DateUtil dateUtil) {
        this.transactionRepository = transactionRepository;
        this.dateUtil = dateUtil;
    }

    @Scheduled(initialDelay = FIVE_MINUTES_IN_MILLIS, fixedDelay = FIVE_MINUTES_IN_MILLIS)
    public void deleteOldData() {
        log.info("Start CleanUpService");
        Collection<Transaction> transactions = transactionRepository.findAll();
        if (CollectionUtils.isEmpty(transactions)) {
            log.info("Where are no old data to delete");
            log.info("Stopped CleanUpService");
            return;
        }

        List<Transaction> oldTransactions = transactions.stream().filter(dateUtil::isTransactionOld).collect(Collectors.toList());

        transactionRepository.deleteAll(oldTransactions);
        log.info("Deleted {} old transactions", oldTransactions.size());
        log.info("Stopped CleanUpService");
    }
}
