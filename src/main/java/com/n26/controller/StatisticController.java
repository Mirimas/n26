package com.n26.controller;

import com.n26.entity.Statistic;
import com.n26.repository.TransactionRepository;
import com.n26.service.DateUtil;
import com.n26.service.StatisticService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Endpoint:
 *  GET /statistics – returns the statistic based of the transactions of the last {@link DateUtil#SECONDS_TRANSACTION_BECOME_OLD} seconds.
 */
@RestController
@RequestMapping(path = "/statistics")
public class StatisticController {
    private final TransactionRepository transactionRepository;
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService, TransactionRepository transactionRepository) {
        this.statisticService = statisticService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * GET /statistics
     * This endpoint returns the statistics computed on the transactions within the last {@link DateUtil#SECONDS_TRANSACTION_BECOME_OLD} seconds.
     *
     * @return
     * {
     *      "sum": "1000.00",
     *      "avg": "100.53",
     *      "max": "200000.49",
     *      "min": "50.23",
     *      "count": 10
     * }
     */
    @RequestMapping(method = GET)
    public Statistic statisticsLastMinute() {
        return statisticService.generateStatisticLastMinute(transactionRepository.findAll());
    }
}
