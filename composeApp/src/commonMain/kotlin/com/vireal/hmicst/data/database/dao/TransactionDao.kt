package com.vireal.hmicst.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vireal.hmicst.data.database.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun observeTransactionsForUser(userId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT COUNT(*) FROM transactions WHERE userId = :userId")
    suspend fun getNumberOfTransactionsForUser(userId: Long): Int

    @Query("SELECT SUM(amount) FROM transactions WHERE userId = :userId AND date = :date")
    fun getTotalSpentForDay(
        userId: Long = 1,
        date: LocalDate,
    ): Flow<Double?>

    @Query("SELECT * FROM transactions WHERE date = :selectedDate")
    suspend fun getTransactionsByDate(selectedDate: LocalDate): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE date = :selectedDate")
    fun getTransactionsFlowByDate(selectedDate: LocalDate): Flow<List<TransactionEntity>>
}
