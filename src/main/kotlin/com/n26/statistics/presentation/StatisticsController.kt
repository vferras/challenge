package com.n26.statistics.presentation

import com.n26.shared.StatisticsStorage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.RoundingMode

@RestController
class StatisticsController {
    @GetMapping("/statistics")
    fun getStatistics(): StatisticResponse {
        if(!StatisticsStorage.isEmpty()) {
            val statistics = StatisticsStorage.getStatisticsFromLast60Seconds()
            return StatisticResponse(
                sum = statistics.sum.toBigDecimalAndString(),
                avg = statistics.average.toBigDecimalAndString(),
                max = statistics.max.toBigDecimalAndString(),
                min = statistics.min.toBigDecimalAndString(),
                count = statistics.count
            )
        }

        return StatisticResponse(
            sum = 0.0.toBigDecimalAndString(),
            avg = 0.0.toBigDecimalAndString(),
            max = 0.0.toBigDecimalAndString(),
            min = 0.0.toBigDecimalAndString(),
            count = 0
        )
    }
}

fun Double.toBigDecimalAndString() = BigDecimal(this).setScale(2, RoundingMode.HALF_UP).toString()
