package com.vireal.hmicst.data.models

import kotlinx.datetime.Clock

data class TransactionModel(
    val id: String,
    val title: String? = null,
    val date: Long = Clock.System.now().toEpochMilliseconds(),
    val amount: Double,
    val categoryId: String,
    val userId: Long,
)
