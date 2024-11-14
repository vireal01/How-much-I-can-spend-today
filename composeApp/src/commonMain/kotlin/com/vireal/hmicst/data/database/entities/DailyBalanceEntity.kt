package com.vireal.hmicst.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "daily_balances")
data class DailyBalanceEntity(
    @PrimaryKey val id: LocalDate,
    val startDateBalance: Double,
    val endDateBalance: Double?,
    val userId: Long = 1,
)
