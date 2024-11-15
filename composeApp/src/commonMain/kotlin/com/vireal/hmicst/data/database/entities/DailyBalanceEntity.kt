package com.vireal.hmicst.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "daily_balances")
data class DailyBalanceEntity(
    @PrimaryKey val date: LocalDate,
// TODO: Find out do I really have to have startDateBalance
    val startDateBalance: Double,
    val endDateBalance: Double,
    val userId: Long = 1,
)
