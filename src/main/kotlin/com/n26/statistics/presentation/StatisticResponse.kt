package com.n26.statistics.presentation

import com.fasterxml.jackson.annotation.JsonProperty

data class StatisticResponse(
        @JsonProperty("sum") val sum: String,
        @JsonProperty("avg") val avg: String,
        @JsonProperty("max") val max: String,
        @JsonProperty("min") val min: String,
        @JsonProperty("count") val count: Long
)
