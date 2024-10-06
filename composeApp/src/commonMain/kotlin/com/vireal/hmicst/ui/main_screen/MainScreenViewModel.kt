package com.vireal.hmicst.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.data.repository.TransactionRepository
import com.vireal.hmicst.utils.mapTransactionEntityToTransactionModel
import com.vireal.hmicst.utils.mapTransactionModelToTransactionEntity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MainScreenViewModel(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    private var _allTransactions = MutableStateFlow<List<TransactionModel>>(mutableListOf())
    val allTransactions: StateFlow<List<TransactionModel>> = _allTransactions

    val exceptionHandler =
        CoroutineExceptionHandler { _, e ->
            println("Error, $e")
        }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            transactionRepository.observeTransactionsForUser(0).collectLatest { transactions ->

                _allTransactions.value =
                    transactions.map { task -> mapTransactionEntityToTransactionModel(task) }
            }
        }
    }

    // insert into room
    @OptIn(ExperimentalUuidApi::class)
    fun addMockUser() =
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            println("adding new transaction")
            val transaction =
                TransactionModel(
                    id = Uuid.random().toString(),
                    title = "test1",
                    amount = 200.0,
                    categoryId = 123.toString(),
                    userId = 0,
                )
            transactionRepository.insertTransaction(mapTransactionModelToTransactionEntity(transaction))
        }
}
