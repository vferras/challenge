package com.n26.statistics;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.DoubleSummaryStatistics;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@ToString
@Getter
public class Statistics {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private Long count;

    public Statistics(DoubleSummaryStatistics doubleSummaryStatistics) {
        this.avg = new BigDecimal(doubleSummaryStatistics.getAverage()).setScale(2, RoundingMode.HALF_UP);
        this.sum = new BigDecimal(doubleSummaryStatistics.getSum()).setScale(2, RoundingMode.HALF_UP);
        this.max = new BigDecimal(doubleSummaryStatistics.getMax()).setScale(2, RoundingMode.HALF_UP);
        this.min = new BigDecimal(doubleSummaryStatistics.getMin()).setScale(2, RoundingMode.HALF_UP);
        this.count = doubleSummaryStatistics.getCount();
    }

    public static Statistics empty() {
        Statistics statistics = Statistics.of(
                new BigDecimal(0).setScale(2),
                new BigDecimal(0).setScale(2),
                new BigDecimal(0).setScale(2),
                new BigDecimal(0).setScale(2),
                0L);

        return statistics;
    }
}
