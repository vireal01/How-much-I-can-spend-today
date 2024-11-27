package com.vireal.hmicst.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.data.repository.DailyBalanceRepository
import com.vireal.hmicst.data.repository.TransactionRepository
import com.vireal.hmicst.data.repository.UserRepository
import com.vireal.hmicst.ui.models.StubTransaction
import com.vireal.hmicst.utils.getCurrentLocalDate
import com.vireal.hmicst.utils.mapTransactionEntityToTransactionModel
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
    private val useRepository: UserRepository,
    private val dailyBalanceRepository: DailyBalanceRepository,
) : ViewModel() {
    private var _allTransactionsForSelectedDay =
        MutableStateFlow<List<TransactionModel>>(mutableListOf())
    val allTransactionsForSelectedDay: StateFlow<List<TransactionModel>> =
        _allTransactionsForSelectedDay

    private val _todayEndDateBalanceAmount = MutableStateFlow(0.0)
    val todayEndDateBalanceAmount: StateFlow<Double> = _todayEndDateBalanceAmount

    private val _isSelectedDateInited = MutableStateFlow(false)
    val isSelectedDateInited: StateFlow<Boolean> = _isSelectedDateInited

    private val _dailyAllowance = MutableStateFlow(0.0)
    val dailyAllowance: StateFlow<Double> = _dailyAllowance

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

    fun init() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            println("Calling MainScreenViewModel init")
            getOrInitDailyBalancesForToday()
            observeTransactionsForSelectedDate()
            observeTotalSpentForSelectedDate()
            observeRemainingForTodayAmount()
            observeEndDateBalanceForSelectedDate()
            observeIsSelectedDataWasInited()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactionsForSelectedDate() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            selectedDate
                .flatMapLatest { date ->
                    transactionRepository.observeTransactionsFlowForSelectedDate(date)
                }.collect { transactions ->
                    _allTransactionsForSelectedDay.value =
                        transactions.map { mapTransactionEntityToTransactionModel(it) }
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

    private fun observeRemainingForTodayAmount() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            useRepository.observeDailyBalance().collect { dailyAmount ->
                _dailyAllowance.value = dailyAmount
            }
        }
    }

    private fun observeEndDateBalanceForSelectedDate() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            dailyBalanceRepository
                .observeEndDateBalanceForSelectedDate(getCurrentLocalDate())
                .collect { endDateBalanceValue ->
                    _todayEndDateBalanceAmount.value = endDateBalanceValue
                }
        }
    }

    fun selectNextDay() {
        _selectedDate.value = _selectedDate.value.plus(DatePeriod(days = 1))
    }

    fun selectPreviousDay() {
        _selectedDate.value = _selectedDate.value.minus(DatePeriod(days = 1))
    }

    fun selectCurrentDay() {
        _selectedDate.value = getCurrentLocalDate()
    }

    // TODO: Debug method. Should be removed
    fun createDummyTransactionForSelectedDay() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val dummyTransaction = StubTransaction.musicItem.copy(date = selectedDate.value)
            transactionRepository.insertTransaction(dummyTransaction)
            dailyBalanceRepository.addTransaction(
                transaction = dummyTransaction,
                dailyAllowance = dailyAllowance.value,
            )
        }
    }

    fun onISpentNothingOnTheDateClicked() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            dailyBalanceRepository.getOrInitDailyBalanceForSelectedDate(
                date = selectedDate.value,
                dailyAllowance = dailyAllowance.value,
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeIsSelectedDataWasInited() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            selectedDate
                .flatMapLatest { date ->
                    dailyBalanceRepository.observeDailyBalanceForSelectedDate(date, userId = 1)
                }.collect {
                    _isSelectedDateInited.value = it
                }
        }
    }

    private fun getOrInitDailyBalancesForToday() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val currentDateDailyBalance =
                dailyBalanceRepository.getOrInitDailyBalanceForSelectedDate(
                    date = getCurrentLocalDate(),
                    dailyAllowance = dailyAllowance.value,
                )
            _todayEndDateBalanceAmount.value = currentDateDailyBalance.endDateBalance
        }
    }
}
