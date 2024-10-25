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

    @Query("DELETE FROM daily_balance WHERE date = :date")
    suspend fun deleteDailyBalanceEntityByDate(date: LocalDate)

    @Query("SELECT * FROM daily_balance WHERE date = :date")
    suspend fun getDailyBalanceEntity(date: LocalDate): DailyBalanceEntity


    @Query("UPDATE daily_balance SET startDateBalance = :startDateBalance WHERE date = :date")
    suspend fun changeStartDateBalanceForDate(date: LocalDate, startDateBalance: Double)

    @Query("UPDATE daily_balance SET endDateBalance = :endDateBalance WHERE date = :date")
    suspend fun changeEndDateBalanceForDate(date: LocalDate, endDateBalance: Double)

    @Query("""
    UPDATE daily_balance
    SET endDateBalance = (
        SELECT IFNULL(startDateBalance, 0) - IFNULL(SUM(amount), 0)
        FROM transactions
        WHERE date = :selectedDate
        AND userId = :userId
    )
    WHERE date = :selectedDate
""")
    suspend fun updateEndDateAmount(selectedDate: LocalDate, userId: Long)
}
