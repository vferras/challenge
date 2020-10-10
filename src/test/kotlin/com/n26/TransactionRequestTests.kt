package com.n26

import com.n26.transactions.presentation.TransactionRequest
import com.n26.transactions.presentation.TransactionValidatorResult
import com.n26.transactions.presentation.validate
import io.kotlintest.matchers.shouldBe
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class TransactionRequestTests {
    @Test
    fun `given a 60 seconds old transaction when storing it then is accepted by the validator`() {
        val transactionRequest = TransactionRequest("2.0", "2020-10-10T12:00:00.000Z")
        val clock = Clock.fixed(Instant.parse("2020-10-10T12:01:00.000Z"), ZoneId.of("UTC"))

        val result = transactionRequest.validate(clock)

        result shouldBe TransactionValidatorResult.CORRECT
    }

    @Test
    fun `given a 61 seconds old transaction when storing it then is rejected as old by the validator`() {
        val transactionRequest = TransactionRequest("2.0", "2020-10-10T12:00:00.000Z")
        val clock = Clock.fixed(Instant.parse("2020-10-10T12:01:01.000Z"), ZoneId.of("UTC"))

        val result = transactionRequest.validate(clock)

        result shouldBe TransactionValidatorResult.OLD
    }

    @Test
    fun `given a future transaction when storing it then is rejected as future by the validator`() {
        val transactionRequest = TransactionRequest("2.0", "2020-10-10T12:01:00.000Z")
        val clock = Clock.fixed(Instant.parse("2020-10-10T12:00:00.000Z"), ZoneId.of("UTC"))

        val result = transactionRequest.validate(clock)

        result shouldBe TransactionValidatorResult.NOT_PARSABLE_OR_FUTURE
    }

    @Test
    fun `given a malformed date when storing it then is rejected as not parsable by the validator`() {
        val transactionRequest = TransactionRequest("2.0", "XXXXXX")
        val clock = Clock.fixed(Instant.parse("2020-10-10T12:00:00.000Z"), ZoneId.of("UTC"))

        val result = transactionRequest.validate(clock)

        result shouldBe TransactionValidatorResult.NOT_PARSABLE_OR_FUTURE
    }

    @Test
    fun `given an invalid amount when storing it then is rejected as invalid by the validator`() {
        val transactionRequest = TransactionRequest("Test", "2020-10-10T12:00:00.000Z")
        val clock = Clock.fixed(Instant.parse("2020-10-10T12:01:00.000Z"), ZoneId.of("UTC"))

        val result = transactionRequest.validate(clock)

        result shouldBe TransactionValidatorResult.NOT_PARSABLE_OR_FUTURE
    }

    @Test
    fun `given a null amount when storing it then is rejected as invalid by the validator`() {
        val transactionRequest = TransactionRequest(null, "2020-10-10T12:00:00.000Z")
        val clock = Clock.fixed(Instant.parse("2020-10-10T12:01:00.000Z"), ZoneId.of("UTC"))

        val result = transactionRequest.validate(clock)

        result shouldBe TransactionValidatorResult.INVALID
    }
}
