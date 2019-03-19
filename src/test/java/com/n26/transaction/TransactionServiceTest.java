package com.n26.transaction;

import com.n26.helper.TimeHelper;
import com.n26.statistics.Statistics;
import com.n26.statistics.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionServiceTest {

    @Mock
    private StatisticsService statisticsService;

    @Mock
    private TimeHelper timeHelper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void given_valid_transaction_when_save_then_statistics_returned() {

        when(timeHelper.getCurrentMilliseconds()).thenReturn(1552751005L);
        Transaction transaction = Transaction.of(2.9, 1552751000L, timeHelper.getCurrentMilliseconds());
        when(statisticsService.addTransactionToStatistic(transaction)).thenReturn(Statistics.empty());

        boolean result = transactionService.add(transaction);

        assertTrue(result);
    }

    @Test
    public void given_invalid_transaction_when_save_then_statistics_empty() {

        Transaction transaction = Transaction.of(2.9, 1552752L, timeHelper.getCurrentMilliseconds());
        when(timeHelper.getCurrentMilliseconds()).thenReturn(1552751L);
        when(statisticsService.addTransactionToStatistic(transaction)).thenReturn(Statistics.empty());

        boolean result = transactionService.add(transaction);

        assertFalse(result);
    }
}
