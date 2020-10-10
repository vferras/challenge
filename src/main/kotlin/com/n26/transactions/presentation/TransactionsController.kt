package com.n26.transactions.presentation

import com.n26.shared.StatisticsStorage
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.Clock

@RestController
class TransactionsController {
    @PostMapping(value = ["/transactions"], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun addTransaction(@RequestBody request: TransactionRequest): ResponseEntity<Unit> {
        return when(request.validate(Clock.systemUTC())) {
            TransactionValidatorResult.NOT_PARSABLE_OR_FUTURE -> ResponseEntity.unprocessableEntity().build()
            TransactionValidatorResult.OLD -> ResponseEntity.noContent().build()
            TransactionValidatorResult.INVALID -> ResponseEntity.badRequest().build()
            TransactionValidatorResult.CORRECT -> {
                StatisticsStorage.addTransactionToStorage(request.toDomain())
                ResponseEntity.created(URI("")).build()
            }
        }
    }

    @DeleteMapping(value = ["/transactions"])
    fun deleteTransactions(): ResponseEntity<Unit> {
        StatisticsStorage.resetStorage()
        return ResponseEntity.noContent().build()
    }
}
