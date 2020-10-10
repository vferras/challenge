package com.n26.transactions.domain

import java.time.LocalDateTime

data class Transaction(val amount: Double, val timestamp: LocalDateTime)
