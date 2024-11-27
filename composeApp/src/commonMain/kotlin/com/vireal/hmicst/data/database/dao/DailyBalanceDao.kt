package com.vireal.hmicst.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vireal.hmicst.data.database.entities.DailyBalanceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface DailyBalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyBalance(dailyBalanceEntity: DailyBalanceEntity)

    @Query("SELECT * FROM daily_balances WHERE date = :selectedDate")
    suspend fun getDailyBalancesForSelectedDate(selectedDate: LocalDate): DailyBalanceEntity?

    @Query(
        "UPDATE daily_balances SET startDateBalance = :startDateBalance, endDateBalance = :endDateBalance WHERE userId = :userId",
    )
    suspend fun updateDailyBalancesFully(
        userId: Long = 1,
        startDateBalance: Double?,
        endDateBalance: Double,
    )

    @Query(
        "UPDATE daily_balances SET startDateBalance = :startDateBalance WHERE date = :date AND userId = :userId",
    )
    suspend fun updateDailyBalanceStartDateValue(
        userId: Long = 1,
        date: LocalDate,
        startDateBalance: Double?,
    )

    @Query(
        "UPDATE daily_balances SET startDateBalance = startDateBalance + :transactionAmount WHERE date = :date AND userId = :userId",
    )
    suspend fun appendToDailyBalanceStartDateValue(
        userId: Long = 1,
        date: LocalDate,
        transactionAmount: Double,
    )

    @Query(
        "UPDATE daily_balances SET endDateBalance = :endDateBalance WHERE date = :date AND userId = :userId",
    )
    suspend fun updateDailyBalanceEndDateValue(
        userId: Long = 1,
        date: LocalDate,
        endDateBalance: Double,
    )

    @Query(
        "UPDATE daily_balances SET endDateBalance = endDateBalance + :transactionAmount WHERE date = :date AND userId = :userId",
    )
    suspend fun appendToDailyBalanceEndDateValue(
        userId: Long = 1,
        date: LocalDate,
        transactionAmount: Double,
    )

    @Query("SELECT * FROM daily_balances WHERE date < :currentDate ORDER BY date DESC LIMIT 1")
    suspend fun getLastDailyBalanceBeforeCurrentDate(currentDate: LocalDate): DailyBalanceEntity?

    @Query("SELECT endDateBalance FROM daily_balances WHERE userId = :userId AND date = :date")
    fun observeEndDateBalanceForSelectedDate(
        date: LocalDate,
        userId: Long,
    ): Flow<Double>

    @Query("SELECT COUNT(*) > 0 FROM daily_balances WHERE userId = :userId AND date = :date")
    fun hasDailyBalanceForSelectedDate(
        date: LocalDate,
        userId: Long,
    ): Flow<Boolean>
}
