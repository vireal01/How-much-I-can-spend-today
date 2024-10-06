package com.vireal.hmicst.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startDate: Long?,
    val yearlyNetIncome: Double?,
    val recurrentSpendings: Double?,
    val savingsGoal: Double?,
)
