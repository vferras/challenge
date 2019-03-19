package com.n26.statistics;

import com.n26.helper.TimeHelper;
import com.n26.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.DoubleSummaryStatistics;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticsServiceTest {

    private StatisticsService statisticsService;
    private TimeHelper timeHelper;

    @Before
    public void Setup() {
        timeHelper = mock(TimeHelper.class);
        statisticsService = new StatisticsService(new StatisticsDao(), timeHelper);
    }

    @Test
    public void given_a_valid_transaction_when_saving_it_as_statistic_then_statistic_is_correctly_calculated() {

        Statistics statistics = statisticsService.addTransactionToStatistic(Transaction.of(232.0, 3322L, timeHelper.getCurrentMilliseconds()));

        assertThat(statistics.getCount(), is(1L));
        assertThat(statistics.getAvg().doubleValue(), is(232.0));
        assertThat(statistics.getSum().doubleValue(), is(232.0));
        assertThat(statistics.getMin().doubleValue(), is(232.0));
        assertThat(statistics.getMax().doubleValue(), is(232.0));
    }

    @Test
    public void given_valid_transactions_when_saving_them_as_statistic_then_statistics_are_correctly_calculated() {

        statisticsService.addTransactionToStatistic(Transaction.of(12.0, 56987L, timeHelper.getCurrentMilliseconds()));
        statisticsService.addTransactionToStatistic(Transaction.of(128.0, 56988L, timeHelper.getCurrentMilliseconds()));
        Statistics statistics = statisticsService.addTransactionToStatistic(Transaction.of(80.0, 56989L, timeHelper.getCurrentMilliseconds()));

        assertThat(statistics.getCount(), is(3L));
        assertThat(statistics.getAvg().doubleValue(), is(73.33));
        assertThat(statistics.getSum().doubleValue(), is(220.0));
        assertThat(statistics.getMin().doubleValue(), is(12.0));
        assertThat(statistics.getMax().doubleValue(), is(128.0));
    }

    @Test
    public void given_two_valid_transactions_and_one_old_when_cleaning_then_the_old_one_is_removed_and_statistics_are_up_to_date() {

        when(timeHelper.getCurrentMilliseconds()).thenReturn(12345000L);

        statisticsService.addTransactionToStatistic(Transaction.of(87.0, 12000000L, timeHelper.getCurrentMilliseconds()));
        statisticsService.addTransactionToStatistic(Transaction.of(123.0, 12340000L, timeHelper.getCurrentMilliseconds()));
        statisticsService.addTransactionToStatistic(Transaction.of(534.0, 12341000L, timeHelper.getCurrentMilliseconds()));

        DoubleSummaryStatistics statistics = statisticsService.removeOutdatedStatistics();

        assertThat(statistics.getCount(), is(2L));
        assertThat(statistics.getAverage(), is(328.5));
        assertThat(statistics.getSum(), is(657.0));
        assertThat(statistics.getMin(), is(123.0));
        assertThat(statistics.getMax(), is(534.0));
    }

    @Test
    public void given_three_valid_transactions_when_cleaning_then_statistics_are_up_to_date() {

        when(timeHelper.getCurrentMilliseconds()).thenReturn(12345L);

        statisticsService.addTransactionToStatistic(Transaction.of(874.0, 12344L, timeHelper.getCurrentMilliseconds()));
        statisticsService.addTransactionToStatistic(Transaction.of(457.0, 12343L, timeHelper.getCurrentMilliseconds()));
        statisticsService.addTransactionToStatistic(Transaction.of(982.0, 12341L, timeHelper.getCurrentMilliseconds()));

        DoubleSummaryStatistics statistics = statisticsService.removeOutdatedStatistics();

        assertThat(statistics.getCount(), is(3L));
        assertThat(statistics.getAverage(), is(771.0));
        assertThat(statistics.getSum(), is(2313.0));
        assertThat(statistics.getMin(), is(457.0));
        assertThat(statistics.getMax(), is(982.0));
    }
}