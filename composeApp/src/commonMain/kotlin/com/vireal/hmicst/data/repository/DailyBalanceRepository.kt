package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.DailyBalanceDao
import com.vireal.hmicst.data.database.entities.DailyBalanceEntity
import com.vireal.hmicst.data.models.DailyBalanceModel
import com.vireal.hmicst.utils.getCurrentLocalDate
import com.vireal.hmicst.utils.mapDailyBalanceEntityToDailyBalanceModel
import com.vireal.hmicst.utils.mapDailyBalanceModelToDailyBalanceEntity
import kotlinx.datetime.LocalDate

class DailyBalanceRepository(
    private val dailyBalanceDao: DailyBalanceDao,
) {
    suspend fun getDailyBalances(date: LocalDate): DailyBalanceModel? {
        val entity: DailyBalanceEntity? = dailyBalanceDao.getDailyBalancesForSelectedDate(date)
        return if (entity == null) {
            null
        } else {
            mapDailyBalanceEntityToDailyBalanceModel(entity)
        }
    }

    suspend fun createDailyBalance(dailyBalanceModel: DailyBalanceModel) {
        dailyBalanceDao.insertDailyBalance(mapDailyBalanceModelToDailyBalanceEntity(dailyBalanceModel))
    }

    suspend fun updateDailyBalanceStartDateValue(dailyBalanceStartDate: Double) =
        dailyBalanceDao.updateDailyBalanceStartDateValue(startDateBalance = dailyBalanceStartDate)

    suspend fun updateDailyBalanceEndDateValue(dailyBalanceEndDate: Double) =
        dailyBalanceDao.updateDailyBalanceStartDateValue(startDateBalance = dailyBalanceEndDate)

    suspend fun getLastBalanceBeforeDate(date: LocalDate = getCurrentLocalDate()): DailyBalanceModel? {
        val lastBalanceEntity = dailyBalanceDao.getLastDailyBalanceBeforeCurrentDate(date)
        return lastBalanceEntity?.let { mapDailyBalanceEntityToDailyBalanceModel(it) }
    }

    private suspend fun initializeTodayBalance(): DailyBalanceModel {
        val currentDate = getCurrentLocalDate()
        val existingBalance = dailyBalanceDao.getDailyBalancesForSelectedDate(currentDate)
        if (existingBalance != null) {
            return mapDailyBalanceEntityToDailyBalanceModel(existingBalance)
        }

        val lastBalance = getLastBalanceBeforeDate()
        val startDateBalance = lastBalance?.endDateBalance ?: 0.0

        val newBalanceEntity =
            DailyBalanceEntity(
                id = currentDate,
                startDateBalance = startDateBalance,
                endDateBalance = null,
            )
        dailyBalanceDao.insertDailyBalance(newBalanceEntity)

        return mapDailyBalanceEntityToDailyBalanceModel(newBalanceEntity)
    }

    suspend fun getOrInitCurrentDateDailyBalance(): DailyBalanceModel = getDailyBalances(getCurrentLocalDate()) ?: initializeTodayBalance()

    suspend fun calculateEndDateBalanceForSelectedDay(
        date: LocalDate,
        dailyBalance: Double,
        transactionsSumForTheDate: Double,
    ) {
        println("date $date, dailyBalance $dailyBalance transactionsSumForTheDate $transactionsSumForTheDate")
        val startDateBalance = dailyBalanceDao.getDailyBalancesForSelectedDate(date)?.startDateBalance ?: return
        val endDateBalance = startDateBalance + dailyBalance - transactionsSumForTheDate
        dailyBalanceDao.updateDailyBalanceEndDateValue(date = date, endDateBalance = endDateBalance)
    }
}
