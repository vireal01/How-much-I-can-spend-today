package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.DailyBalanceDao
import com.vireal.hmicst.data.database.entities.DailyBalanceEntity
import com.vireal.hmicst.data.models.DailyBalanceModel
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.utils.getCurrentLocalDate
import com.vireal.hmicst.utils.mapDailyBalanceEntityToDailyBalanceModel
import com.vireal.hmicst.utils.mapDailyBalanceModelToDailyBalanceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn
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

    private suspend fun createDailyBalance(dailyBalanceModel: DailyBalanceModel) {
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

    private suspend fun getLastBalanceBeforeDate(date: LocalDate = getCurrentLocalDate()): DailyBalanceModel? {
        val lastBalanceEntity = dailyBalanceDao.getLastDailyBalanceBeforeCurrentDate(date)
        return lastBalanceEntity?.let { mapDailyBalanceEntityToDailyBalanceModel(it) }
    }

    private suspend fun getOrInitializeDailyBalanceForSelectedDate(
        date: LocalDate,
        dailyAllowance: Double,
    ): DailyBalanceModel {
        val existingBalance = dailyBalanceDao.getDailyBalancesForSelectedDate(date)
        if (existingBalance != null) {
            return mapDailyBalanceEntityToDailyBalanceModel(existingBalance)
        }

        val lastBalance = getLastBalanceBeforeDate(date)
        val lastDateEndBalance = lastBalance?.endDateBalance ?: 0.0
        val endDateBalance = lastDateEndBalance + dailyAllowance

        val newBalanceModel =
            DailyBalanceModel(
                date = date,
                startDateBalance = endDateBalance,
                endDateBalance = endDateBalance,
            )
        createDailyBalance(newBalanceModel)

        // Add daily allowance if the transaction was made in the past and it should be calculated as well
        addDailyAllowanceForTodayIfTransactionMadeInThePast(
            transactionDate = date,
            dailyAllowance = dailyAllowance,
        )

        return newBalanceModel
    }

    private suspend fun addDailyAllowanceForTodayIfTransactionMadeInThePast(
        transactionDate: LocalDate,
        dailyAllowance: Double,
    ) {
        if (transactionDate < getCurrentLocalDate()) {
            dailyBalanceDao.appendToDailyBalanceEndDateValue(
                date = getCurrentLocalDate(),
                transactionAmount = dailyAllowance,
            )
        }
    }

    suspend fun getOrInitDailyBalanceForSelectedDate(
        date: LocalDate = getCurrentLocalDate(),
        dailyAllowance: Double,
    ): DailyBalanceModel =
        getDailyBalances(date) ?: getOrInitializeDailyBalanceForSelectedDate(
            date = date,
            dailyAllowance = dailyAllowance,
        )

    suspend fun addTransaction(
        transaction: TransactionModel,
        dailyAllowance: Double,
    ) {
        getOrInitDailyBalanceForSelectedDate(getCurrentLocalDate(), dailyAllowance)
        if (transaction.date < getCurrentLocalDate()) {
            getOrInitializeDailyBalanceForSelectedDate(
                date = transaction.date,
                dailyAllowance = dailyAllowance,
            )
            dailyBalanceDao.appendToDailyBalanceEndDateValue(
                date = transaction.date,
                transactionAmount = transaction.amount,
            )
        }

        dailyBalanceDao.appendToDailyBalanceEndDateValue(
            date = getCurrentLocalDate(),
            transactionAmount = transaction.amount,
        )
    }

    fun observeEndDateBalanceForSelectedDate(
        date: LocalDate,
        userId: Long = 1,
    ) = dailyBalanceDao.observeEndDateBalanceForSelectedDate(date = date, userId = userId).flowOn(
        Dispatchers.IO,
    )

    fun observeDailyBalanceForSelectedDate(
        date: LocalDate,
        userId: Long = 1,
    ) = dailyBalanceDao.hasDailyBalanceForSelectedDate(date = date, userId = userId).flowOn(
        Dispatchers.IO,
    )
}
