package com.vireal.hmicst.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vireal.hmicst.data.database.entities.DailyBalanceEntity
import kotlinx.datetime.LocalDate

@Dao
interface DailyBalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyBalance(dailyBalanceEntity: DailyBalanceEntity)

    @Query("SELECT * FROM daily_balances WHERE id = :selectedDate")
    suspend fun getDailyBalancesForSelectedDate(selectedDate: LocalDate): DailyBalanceEntity?

    @Query(
        "UPDATE daily_balances SET startDateBalance = :startDateBalance, endDateBalance = :endDateBalance WHERE id = :userId",
    )
    suspend fun updateDailyBalancesFully(
        userId: Long = 1,
        startDateBalance: Double?,
        endDateBalance: Double,
    )

    @Query(
        "UPDATE daily_balances SET startDateBalance = :startDateBalance WHERE id = :userId",
    )
    suspend fun updateDailyBalanceStartDateValue(
        userId: Long = 1,
        startDateBalance: Double?,
    )

    @Query(
        "UPDATE daily_balances SET endDateBalance = :endDateBalance WHERE id = :date AND userId = :userId",
    )
    suspend fun updateDailyBalanceEndDateValue(
        userId: Long = 1,
        date: LocalDate,
        endDateBalance: Double,
    )

    @Query("SELECT * FROM daily_balances WHERE id < :currentDate ORDER BY id DESC LIMIT 1")
    suspend fun getLastDailyBalanceBeforeCurrentDate(currentDate: LocalDate): DailyBalanceEntity?
}
