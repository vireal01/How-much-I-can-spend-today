package com.vireal.hmicst.data.models

import kotlinx.datetime.LocalDate

class DailyBalanceModel(
    val id: LocalDate,
    val startDateBalance: Double,
    val endDateBalance: Double?,
    val userId: Long = 1,
)
