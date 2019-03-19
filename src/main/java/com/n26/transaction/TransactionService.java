package com.n26.transaction;

import com.n26.statistics.StatisticsService;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private StatisticsService statisticsService;

    public TransactionService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    boolean add(Transaction transaction) {
        if(transaction.isLastSixtyMinutesTransaction(transaction.getCurrentTimestamp())){
            statisticsService.addTransactionToStatistic(transaction);
            return true;
        }

        return  false;
    }

    void deleteAll() {
        statisticsService.deleteStatistics();
    }
}
