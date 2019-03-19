package com.n26.statistics;

import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatisticsDao {
    private ConcurrentHashMap<Long, DoubleSummaryStatistics> statisticsStoredPerSecond = new ConcurrentHashMap<>();

    DoubleSummaryStatistics updateStatisticsInCollection(Long timeKey, Double value){
        synchronized (this) {
            DoubleSummaryStatistics statistics = getStatisticsFromCollection(timeKey);

            statistics.accept(value);

            return statisticsStoredPerSecond.put(timeKey, statistics);
        }
    }

    void removeStatisticsInCollection(Long timeKey) {
        for(Map.Entry<Long, DoubleSummaryStatistics> entry : statisticsStoredPerSecond.entrySet()) {
            if(entry.getKey() < timeKey) {
                statisticsStoredPerSecond.remove(entry.getKey());
            }
        }
    }

    void removeAllStatisticsInCollection() {
        statisticsStoredPerSecond = new ConcurrentHashMap<>();
    }

    DoubleSummaryStatistics getStatisticsFromCollection(Long timeKey){
        DoubleSummaryStatistics statisticFromCollection = statisticsStoredPerSecond.get(timeKey);
        return statisticFromCollection == null ? new DoubleSummaryStatistics() : statisticFromCollection;
    }

    DoubleSummaryStatistics refreshStatistics() {
        DoubleSummaryStatistics newAggregatedStatistics = new DoubleSummaryStatistics();

        for(Map.Entry<Long, DoubleSummaryStatistics> statisticEntry : statisticsStoredPerSecond.entrySet()) {
            newAggregatedStatistics.combine(statisticEntry.getValue());
        }

        return newAggregatedStatistics;
    }

    boolean anyStatisticStored() {
        return !statisticsStoredPerSecond.isEmpty();
    }
}
