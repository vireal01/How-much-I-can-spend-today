package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.UserDao
import com.vireal.hmicst.data.database.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock

class UserRepository(
    private val userDao: UserDao,
) {
    suspend fun getUser(): UserEntity? = userDao.getUserById(1)

    suspend fun createDefaultUser() {
        val defaultUser =
            UserEntity(
                startDate = Clock.System.now().toEpochMilliseconds(),
                yearlyNetIncome = null,
                recurrentSpendings = null,
                savingsGoal = null,
                dailyBalance = 0.0,
            )
        userDao.insertUser(defaultUser)
    }

    suspend fun updateUserData(
        yearlyNetIncome: Double?,
        savingsGoal: Double?,
        recurrentSpendings: Double?,
        dailyBalance: Double,
    ) {
        userDao.updateUser(
            yearlyNetIncome = yearlyNetIncome,
            recurrentSpendings = recurrentSpendings,
            savingsGoal = savingsGoal,
            dailyBalance = dailyBalance,
        )
    }

    suspend fun deleteUserById(userId: Long) {
        userDao.deleteUserById(userId)
    }

    // TODO: Rename dailyBalance to dailyAvailableAmount
    fun observeDailyBalance(userId: Long = 1) = userDao.observeDailyBalance(userId).flowOn(Dispatchers.IO)
}
