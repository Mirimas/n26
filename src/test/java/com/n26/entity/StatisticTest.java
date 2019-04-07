package com.n26.entity;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class StatisticTest {

    @Test
    public void createDefault() {
        Statistic statistic = new Statistic();
        assertEquals(BigDecimal.ZERO, statistic.getAvg());
        assertEquals(BigDecimal.ZERO, statistic.getMax());
        assertEquals(BigDecimal.ZERO, statistic.getMin());
        assertEquals(BigDecimal.ZERO, statistic.getSum());
        assertEquals(0, statistic.getCount());
    }

}