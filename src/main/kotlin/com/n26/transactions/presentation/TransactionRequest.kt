package com.n26.transactions.presentation

import com.fasterxml.jackson.annotation.JsonProperty
import com.n26.transactions.domain.Transaction
import org.springframework.beans.factory.annotation.Autowired
import java.time.Clock
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class TransactionRequest(
    @JsonProperty("amount") val amount: String? = null,
    @JsonProperty("timestamp") val timestamp: String? = null
)

fun TransactionRequest.validate(@Autowired clock: Clock): TransactionValidatorResult {
    if(this.amount == null || this.timestamp == null) return TransactionValidatorResult.INVALID

    try {
        if(this.amount.toDoubleOrNull() == null)
            return TransactionValidatorResult.NOT_PARSABLE_OR_FUTURE
        if(this.parseTimeStamp().plusSeconds(60).isBefore(ChronoLocalDateTime.from(LocalDateTime.now(clock))))
            return TransactionValidatorResult.OLD
        if(this.parseTimeStamp().isAfter(ChronoLocalDateTime.from(LocalDateTime.now(clock))))
            return TransactionValidatorResult.NOT_PARSABLE_OR_FUTURE
    } catch (e: DateTimeParseException) {
        return TransactionValidatorResult.NOT_PARSABLE_OR_FUTURE
    }

    return TransactionValidatorResult.CORRECT
}

fun TransactionRequest.toDomain(): Transaction {
    return try {
        Transaction(this.amount!!.toDouble(), this.parseTimeStamp())
    } catch (e: NumberFormatException) {
        Transaction(-1.0, LocalDateTime.MIN)
    }
}

fun TransactionRequest.parseTimeStamp(): LocalDateTime {
    return LocalDateTime.parse(this.timestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

enum class TransactionValidatorResult {
    CORRECT, NOT_PARSABLE_OR_FUTURE, OLD, INVALID
}
