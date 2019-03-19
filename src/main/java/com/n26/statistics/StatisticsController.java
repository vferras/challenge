package com.n26.statistics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private StatisticsService statisticsService;

    StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(value = "/statistics")
    public StatisticsResponse GetStatistics() {
        return StatisticsResponse.map(statisticsService.getStatistics());
    }
}
