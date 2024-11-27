package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.TransactionDao
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.utils.mapTransactionModelToTransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.LocalDate

class TransactionRepository(
    private val transactionDao: TransactionDao,
) {
    fun observeTransactionsForUser(userId: Long = 1) = transactionDao.observeTransactionsForUser(userId).flowOn(Dispatchers.IO)

    fun observeTransactionsFlowForSelectedDate(selectedDate: LocalDate) =
        transactionDao.getTransactionsFlowByDate(selectedDate).flowOn(Dispatchers.IO)

    suspend fun getTransactionsForSelectedDate(
        userId: Long = 1,
        selectedDate: LocalDate,
    ) = transactionDao.getTransactionsByDate(selectedDate)

    suspend fun getNumberOfTransactionsForUser(userId: Long = 1) = transactionDao.getNumberOfTransactionsForUser(userId)

    suspend fun insertTransaction(transactionModel: TransactionModel) {
        transactionDao.insertTransaction(mapTransactionModelToTransactionEntity(transactionModel))
    }

    fun getTotalSpentForSelectedDate(date: LocalDate) = transactionDao.getTotalSpentForDay(date = date).flowOn(Dispatchers.IO)

    suspend fun getSumOfAllTransactionsForSelectedDate(date: LocalDate): Double =
        transactionDao
            .getTransactionsByDate(date)
            .map { it.amount }
            .reduceOrNull { acc, transactionAmount -> acc + transactionAmount }
            ?: 0.0
}
// TODO: Add daily amount for all skipped days, fix Remaining for today state
