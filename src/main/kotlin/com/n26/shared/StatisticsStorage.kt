package com.n26.shared

import com.n26.transactions.domain.Transaction
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.DoubleSummaryStatistics

object StatisticsStorage {
    private var storage: MutableList<HashMap<Long, Double>> = mutableListOf()

    fun resetStorage() {
        synchronized(storage) { storage = mutableListOf() }
    }

    fun addTransactionToStorage(transaction: Transaction) {
        synchronized(storage) {
            storage.add(hashMapOf(transaction.timestamp.toEpochSecond(ZoneOffset.UTC) to transaction.amount))
        }
    }

    fun getStatisticsFromLast60Seconds(): DoubleSummaryStatistics {
        val statistics = DoubleSummaryStatistics()
        val currentTime = LocalDateTime.now(Clock.systemUTC()).toEpochSecond(ZoneOffset.ofHours(0))

        storage.forEach { hashMap ->
            hashMap
                .filterKeys { timestamp -> timestamp in (currentTime - 59)..currentTime }
                .values
                .map { amount -> statistics.accept(amount) }
        }

        return statistics
    }

    fun isEmpty() = run { storage.count() == 0 }
}
