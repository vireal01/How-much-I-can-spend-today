package com.vireal.hmicst.data.repository

import com.vireal.hmicst.data.database.dao.TransactionDao
import com.vireal.hmicst.data.database.entities.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn

class TransactionRepository(
    private val transactionDao: TransactionDao,
) {
    fun observeTransactionsForUser(userId: Long = 0) = transactionDao.observeTransactionsForUser(userId).flowOn(Dispatchers.IO)

    suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        transactionDao.insertTransaction(transactionEntity)
    }
}
