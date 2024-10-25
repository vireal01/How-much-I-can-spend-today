package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.DailyBalanceDao
import com.vireal.hmicst.data.database.entities.DailyBalanceEntity
import kotlinx.datetime.LocalDate

class DailyBalanceRepository(
    private val dailyBalanceDao: DailyBalanceDao,
) {
    suspend fun getDailyBalanceEntityForDate(date: LocalDate) = dailyBalanceDao.getDailyBalanceEntity(date)

    suspend fun insertDailyBalance(dailyBalanceEntity: DailyBalanceEntity) {
        dailyBalanceDao.insertDailyBalance(dailyBalanceEntity)
    }

    suspend fun changeEndDateBalance(date: LocalDate, endDateBalance: Double) {
        dailyBalanceDao.changeEndDateBalanceForDate(date, endDateBalance)
    }

    suspend fun changeStartDateBalance(date: LocalDate, startDateBalance: Double) {
        dailyBalanceDao.changeStartDateBalanceForDate(date, startDateBalance)
    }

    suspend fun updateEndDateAmount(date: LocalDate, userId: Long) {
        dailyBalanceDao.updateEndDateAmount(date, userId)
    }
}
