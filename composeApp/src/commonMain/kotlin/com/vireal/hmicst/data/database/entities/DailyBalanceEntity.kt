package com.vireal.hmicst.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "daily_balance")
data class DailyBalanceEntity(
    @PrimaryKey(autoGenerate = false) val date: LocalDate,
    val startDateBalance: Double,
    val endDateBalance: Double,
)
