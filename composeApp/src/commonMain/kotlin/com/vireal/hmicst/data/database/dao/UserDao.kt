package com.vireal.hmicst.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vireal.hmicst.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Long)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query(
        "UPDATE users SET yearlyNetIncome = :yearlyNetIncome, recurrentSpendings = :recurrentSpendings, savingsGoal = :savingsGoal, dailyBalance = :dailyBalance WHERE id = :userId",
    )
    suspend fun updateUser(
        userId: Long = 1,
        yearlyNetIncome: Double?,
        recurrentSpendings: Double?,
        savingsGoal: Double?,
        dailyBalance: Double = 0.0
    )

    @Query("SELECT dailyBalance FROM users WHERE id = :userId")
    fun observeDailyBalance(userId: Long): Flow<Double>
}
