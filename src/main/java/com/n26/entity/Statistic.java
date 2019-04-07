package com.n26.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.n26.serializer.MoneySerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistic {
    /**
     *  The total sum of transaction value in the last 60 seconds
     */
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal sum = BigDecimal.ZERO;

    /**
     *  The average amount of transaction value in the last 60 seconds
     */
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal avg = BigDecimal.ZERO;

    /**
     * Single highest transaction value in the last 60 seconds
     */
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal max = BigDecimal.ZERO;

    /**
     * Single lowest transaction value in the last 60 seconds
     */
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal min = BigDecimal.ZERO;

    /**
     * The total number of transactions that happened in the last 60 seconds
     */
    private long count = 0;
}
