package com.vireal.hmicst.data.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class TransactionModel(
    val id: String? = null,
    val title: String? = null,
    val date: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date,
    val amount: Double,
    val categoryId: String,
    val userId: Long = 1,
)
