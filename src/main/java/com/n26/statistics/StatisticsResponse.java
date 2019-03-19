package com.n26.statistics;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class StatisticsResponse {
    public String sum;
    public String avg;
    public String max;
    public String min;
    public Long count;

    static StatisticsResponse map(Statistics statistics) {
        return StatisticsResponse.of(
                statistics.getSum().toString(),
                statistics.getAvg().toString(),
                statistics.getMax().toString(),
                statistics.getMin().toString(),
                statistics.getCount());
    }
}
