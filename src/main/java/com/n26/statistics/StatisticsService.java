package com.n26.statistics;

import com.n26.helper.TimeHelper;
import com.n26.transaction.Transaction;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;

@Service
public class StatisticsService {
    private DoubleSummaryStatistics statisticsSummary;
    private StatisticsDao statisticsDao;
    private TimeHelper timeHelper;

    StatisticsService(StatisticsDao statisticsDao, TimeHelper timeHelper) {
        this.statisticsSummary = new DoubleSummaryStatistics();
        this.statisticsDao = statisticsDao;
        this.timeHelper = timeHelper;
    }

    public Statistics addTransactionToStatistic(Transaction transaction) {
        statisticsSummary.accept(transaction.getAmount());

        Statistics statistics = new Statistics(statisticsSummary);

        statisticsDao.updateStatisticsInCollection(transaction.getTimestamp(), transaction.getAmount());

        return statistics;
    }

    public DoubleSummaryStatistics removeOutdatedStatistics() {
        long nowInMilliseconds = timeHelper.getCurrentMilliseconds();
        long firstElementInCollection = nowInMilliseconds - TimeHelper.MAX_MILLISECONDS;

        statisticsDao.removeStatisticsInCollection(firstElementInCollection);

        statisticsSummary = statisticsDao.refreshStatistics();

        return statisticsSummary;
    }

    public Statistics getStatistics() {
        return statisticsDao.anyStatisticStored() ? new Statistics(statisticsSummary) : Statistics.empty();
    }

    public void deleteStatistics() {
        statisticsSummary = new DoubleSummaryStatistics();
        statisticsDao.removeAllStatisticsInCollection();
    }
}
