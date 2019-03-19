package com.n26.statistics;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class StatisticsCleanupRunnable implements Runnable, DisposableBean {

    private StatisticsService statisticsService;
    private StatisticsDao statisticsDao;

    public StatisticsCleanupRunnable(StatisticsService statisticsService, StatisticsDao statisticsDao) {
        this.statisticsService = statisticsService;
        this.statisticsDao = statisticsDao;
    }

    @Override
    public void run() {
        try {
            statisticsService.removeOutdatedStatistics();

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        statisticsDao.removeAllStatisticsInCollection();
    }
}
