package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.UserDao
import com.vireal.hmicst.data.database.entities.UserEntity
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
            )
        userDao.insertUser(defaultUser)
    }

    suspend fun updateUserData(
        yearlyNetIncome: Double?,
        savingsGoal: Double?,
        recurrentSpendings: Double?,
    ) {
        userDao.updateUser(
            yearlyNetIncome = yearlyNetIncome,
            recurrentSpendings = recurrentSpendings,
            savingsGoal = savingsGoal,
        )
    }

    suspend fun deleteUserById(userId: Long) {
        userDao.deleteUserById(userId)
    }
}
