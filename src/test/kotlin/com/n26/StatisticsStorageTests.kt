package com.n26

import com.n26.shared.StatisticsStorage
import com.n26.transactions.domain.Transaction
import io.kotlintest.matchers.shouldBe
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime

class StatisticsStorageTests {
    @Test
    fun `given statistics when resetting storage then storage is empty`() {
        StatisticsStorage.addTransactionToStorage(Transaction(5.0, LocalDateTime.now(Clock.systemUTC())))
        StatisticsStorage.resetStorage()
        StatisticsStorage.getStatisticsFromLast60Seconds().count shouldBe 0L
    }

    @Test
    fun `given a transaction when adding it into the storage then storage is not empty`() {
        StatisticsStorage.addTransactionToStorage(Transaction(5.0, LocalDateTime.now(Clock.systemUTC())))
        StatisticsStorage.getStatisticsFromLast60Seconds().count shouldBe 1L
    }

    @Test
    fun `given some transactions stored when asking the statistics then they are correctly calculated`() {
        StatisticsStorage.addTransactionToStorage(Transaction(5.0, LocalDateTime.now(Clock.systemUTC())))
        StatisticsStorage.addTransactionToStorage(Transaction(15.0, LocalDateTime.now(Clock.systemUTC())))
        StatisticsStorage.addTransactionToStorage(Transaction(1.0, LocalDateTime.now(Clock.systemUTC())))

        val statistics = StatisticsStorage.getStatisticsFromLast60Seconds()

        statistics.count shouldBe 3L
        statistics.sum shouldBe 21.0
        statistics.min shouldBe 1.0
        statistics.max shouldBe 15.0
        statistics.average shouldBe 7.0
    }
}
