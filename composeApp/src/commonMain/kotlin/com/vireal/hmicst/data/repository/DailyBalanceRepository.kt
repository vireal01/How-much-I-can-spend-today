package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.DailyBalanceDao
import com.vireal.hmicst.data.database.entities.DailyBalanceEntity
import com.vireal.hmicst.data.models.DailyBalanceModel
import com.vireal.hmicst.utils.getCurrentLocalDate
import com.vireal.hmicst.utils.mapDailyBalanceEntityToDailyBalanceModel
import com.vireal.hmicst.utils.mapDailyBalanceModelToDailyBalanceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

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

    suspend fun updateDailyBalanceStartDateValue(
        date: LocalDate,
        dailyBalanceStartDate: Double,
    ) = dailyBalanceDao.updateDailyBalanceStartDateValue(
        date = date,
        startDateBalance = dailyBalanceStartDate,
    )

    suspend fun updateDailyBalanceEndDateValue(
        date: LocalDate,
        dailyBalanceEndDate: Double,
    ) = dailyBalanceDao.updateDailyBalanceStartDateValue(
        date = date,
        startDateBalance = dailyBalanceEndDate,
    )

    suspend fun getLastBalanceBeforeDate(date: LocalDate = getCurrentLocalDate()): DailyBalanceModel? {
        val lastBalanceEntity = dailyBalanceDao.getLastDailyBalanceBeforeCurrentDate(date)
        return lastBalanceEntity?.let { mapDailyBalanceEntityToDailyBalanceModel(it) }
    }

    private suspend fun initializeDailyBalanceForSelectedDate(
        date: LocalDate,
        dailyAllowance: Double,
    ): DailyBalanceModel {
        val existingBalance = dailyBalanceDao.getDailyBalancesForSelectedDate(date)
        if (existingBalance != null) {
            return mapDailyBalanceEntityToDailyBalanceModel(existingBalance)
        }

        val lastBalance = getLastBalanceBeforeDate(date)
        val lastDateEndBalance = lastBalance?.endDateBalance ?: 0.0
        val daysBetween = lastBalance?.date?.let { date.minus(it) }?.days ?: 1
        println("lastDateEndBalance - $lastDateEndBalance, daysBetween - $daysBetween")
        val startDateBalance =
            (lastDateEndBalance.plus(daysBetween * dailyAllowance))
        val endDateBalance =
            startDateBalance // yes, we set endDateBalance equal to startDateBalance during initialization

        val newBalanceEntity =
            DailyBalanceEntity(
                date = date,
                startDateBalance = startDateBalance,
                endDateBalance = endDateBalance,
            )
        dailyBalanceDao.insertDailyBalance(newBalanceEntity)

        return mapDailyBalanceEntityToDailyBalanceModel(newBalanceEntity)
    }

    suspend fun getOrInitDailyBalanceForSelectedDate(
        date: LocalDate = getCurrentLocalDate(),
        dailyAllowance: Double,
    ): DailyBalanceModel =
        getDailyBalances(date) ?: initializeDailyBalanceForSelectedDate(
            date = date,
            dailyAllowance = dailyAllowance,
        )

//    suspend fun calculateEndDateBalanceForSelectedDay(
//        date: LocalDate,
//        dailyBalance: Double,
//        transactionsSumForTheDate: Double,
//    ) {
//        println("date $date, dailyBalance $dailyBalance transactionsSumForTheDate $transactionsSumForTheDate")
//        val startDateBalance =
//            dailyBalanceDao.getDailyBalancesForSelectedDate(date)?.startDateBalance ?: return
//        val endDateBalance = startDateBalance + dailyBalance - transactionsSumForTheDate
//        dailyBalanceDao.updateDailyBalanceEndDateValue(date = date, endDateBalance = endDateBalance)
//    }

    suspend fun recalculateCurrentStartDailyBudgetInCaseTransactionMadeInThePast(
        transactionDate: LocalDate,
        transactionAmount: Double,
        dailyAllowance: Double,
    ) {
        // to make sure that the today's DailyBalance exists
        getOrInitDailyBalanceForSelectedDate(getCurrentLocalDate(), dailyAllowance)
        // If transaction made in the past
        if (transactionDate < getCurrentLocalDate()) {
            if (getDailyBalances(transactionDate) == null) {
                initializeDailyBalanceForSelectedDate(
                    date = transactionDate,
                    dailyAllowance = dailyAllowance,
                )
                // in case of first transaction, add dailyAllowance to today's balance
                dailyBalanceDao.appendToDailyBalanceStartDateValue(
                    date = getCurrentLocalDate(),
                    transactionAmount = dailyAllowance,
                )
                dailyBalanceDao.appendToDailyBalanceEndDateValue(
                    date = getCurrentLocalDate(),
                    transactionAmount = dailyAllowance,
                )
            }
            // Recalculate endDateBalance of the transaction day
            dailyBalanceDao.appendToDailyBalanceEndDateValue(
                date = transactionDate,
                transactionAmount = transactionAmount,
            )

            // Change startDateBalance for today to include transaction from the past in the statistic
            dailyBalanceDao.appendToDailyBalanceStartDateValue(
                date = getCurrentLocalDate(),
                transactionAmount = transactionAmount,
            )
        }
        dailyBalanceDao.appendToDailyBalanceEndDateValue(
            date = getCurrentLocalDate(),
            transactionAmount = transactionAmount,
        )
    }

    fun observeEndDateBalanceForSelectedDate(
        date: LocalDate,
        userId: Long = 1,
    ) = dailyBalanceDao.observeEndDateBalanceForSelectedDate(date = date, userId = userId).flowOn(
        Dispatchers.IO,
    )
}
