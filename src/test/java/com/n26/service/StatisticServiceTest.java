package com.n26.service;

import com.n26.entity.Statistic;
import com.n26.entity.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceTest {
    @Mock
    private DateUtil dateUtil;
    @InjectMocks
    private StatisticService statisticService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createStatisticLastMinute() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.TEN);
        transaction.setTimestamp(Date.from(Instant.now()));

        Transaction transaction2 = new Transaction();
        transaction2.setTimestamp(Date.from(Instant.now()));
        transaction2.setAmount(BigDecimal.ONE);

        Transaction transactionRoundDown = new Transaction();
        transactionRoundDown.setTimestamp(Date.from(Instant.now()));
        transactionRoundDown.setAmount(BigDecimal.valueOf(2.554));

        Transaction transactionRoundUp = new Transaction();
        transactionRoundUp.setTimestamp(Date.from(Instant.now()));
        transactionRoundUp.setAmount(BigDecimal.valueOf(2.556));

        List<Transaction> transactions = Arrays.asList(transaction, transaction2,
                transactionRoundDown, transactionRoundUp);

        Statistic statisticLastMinute = statisticService.generateStatisticLastMinute(transactions);
        assertEquals(4, statisticLastMinute.getCount());
        assertEquals(BigDecimal.valueOf(1), statisticLastMinute.getMin());
        assertEquals(BigDecimal.valueOf(10), statisticLastMinute.getMax());
        assertEquals(BigDecimal.valueOf(16.11).setScale(2), statisticLastMinute.getSum().setScale(2));
        assertEquals(BigDecimal.valueOf(4.03).setScale(2), statisticLastMinute.getAvg().setScale(2));
    }
}