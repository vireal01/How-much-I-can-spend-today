package com.vireal.hmicst.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.data.repository.TransactionRepository
import com.vireal.hmicst.ui.models.StubTransaction
import com.vireal.hmicst.ui.user_income_and_outcome.UserStateViewModel
import com.vireal.hmicst.utils.mapTransactionEntityToTransactionModel
import com.vireal.hmicst.utils.mapTransactionModelToTransactionEntity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class MainScreenViewModel(
    private val transactionRepository: TransactionRepository,
    private val userStateViewModel: UserStateViewModel,
) : ViewModel() {
    private var _allTransactionsForSelectedDay =
        MutableStateFlow<List<TransactionModel>>(mutableListOf())
    val allTransactionsForSelectedDay: StateFlow<List<TransactionModel>> =
        _allTransactionsForSelectedDay

    private val _dailyAvailableMoneyAmount = MutableStateFlow(0.0)
    val dailyAvailableMoneyAmount: StateFlow<Double> = _dailyAvailableMoneyAmount

    private val _totalSpentForSelectedDay = MutableStateFlow(0.0)
    val totalSpentForSelectedDay: StateFlow<Double> = _totalSpentForSelectedDay

    private val _selectedDate: MutableStateFlow<LocalDate> =
        MutableStateFlow(
            Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date,
        )
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val exceptionHandler = CoroutineExceptionHandler { _, e -> println("Error, $e") }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            observeTransactionsForSelectedDate()
            observeTotalSpentForSelectedDate()
            _dailyAvailableMoneyAmount.value = getDailyAvailableMoneyAmount()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactionsForSelectedDate() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            selectedDate
                .flatMapLatest { date ->
                    transactionRepository.observeTransactionsFlowForSelectedDate(date)
                }.collect { transactions ->
                    _allTransactionsForSelectedDay.value = transactions.map { mapTransactionEntityToTransactionModel(it) }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTotalSpentForSelectedDate() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            selectedDate
                .flatMapLatest { date ->
                    transactionRepository.getTotalSpentForSelectedDate(date)
                }.collect { totalSpent ->
                    _totalSpentForSelectedDay.value = totalSpent ?: 0.0
                }
        }
    }

    private fun getDaysInCurrentYear(): Int {
        val currentYear =
            Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date.year
        val endOfYear = LocalDate(currentYear, 12, 31)

        return endOfYear.dayOfYear
    }

    fun updateDailyAvailableMoney() {
        _dailyAvailableMoneyAmount.value = getDailyAvailableMoneyAmount()
    }

    fun getDailyAvailableMoneyAmount(): Double {
        val income = userStateViewModel.yearlyNetIncome.value.toDoubleOrNull() ?: return 0.0
        val spendings = userStateViewModel.recurrentSpendings.value.toDoubleOrNull() ?: return 0.0
        val savings = userStateViewModel.savingsGoal.value.toDoubleOrNull() ?: return 0.0
        val daysInYear = getDaysInCurrentYear().takeIf { it > 0 } ?: return 0.0

        println("income - $income, savings -$savings, spendings - $spendings, daysInYear - $daysInYear")

        return ((income - spendings - savings) / daysInYear)
    }

    fun selectNextDay() {
        _selectedDate.value = _selectedDate.value.plus(DatePeriod(days = 1))
    }

    fun selectPreviousDay() {
        _selectedDate.value = _selectedDate.value.minus(DatePeriod(days = 1))
    }

    fun createDummyTransactionForSelectedDay() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val dummyTransaction = StubTransaction.musicItem.copy(date = selectedDate.value)
            transactionRepository.insertTransaction(mapTransactionModelToTransactionEntity(dummyTransaction))
        }
    }
}
