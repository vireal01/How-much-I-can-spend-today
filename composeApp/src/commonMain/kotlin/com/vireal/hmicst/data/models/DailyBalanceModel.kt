package com.vireal.hmicst.data.models

import kotlinx.datetime.LocalDate

class DailyBalanceModel(
    val date: LocalDate,
    val startDateBalance: Double,
    val endDateBalance: Double,
)
