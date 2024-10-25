package com.vireal.hmicst.ui.user_income_and_outcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vireal.hmicst.data.repository.UserRepository
import com.vireal.hmicst.utils.mapUserEntityToUserModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class UserStateViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _yearlyNetIncome = MutableStateFlow("")
    val yearlyNetIncome: StateFlow<String> = _yearlyNetIncome

    private val _recurrentSpendings = MutableStateFlow("")
    val recurrentSpendings: StateFlow<String> = _recurrentSpendings

    private val _savingsGoal = MutableStateFlow("")
    val savingsGoal: StateFlow<String> = _savingsGoal

    private val _yearlyNetIncomeError = MutableStateFlow<String?>(null)
    val yearlyNetIncomeError: StateFlow<String?> = _yearlyNetIncomeError

    private val _recurrentSpendingsError = MutableStateFlow<String?>(null)
    val recurrentSpendingsError: StateFlow<String?> = _recurrentSpendingsError

    private val _savingsGoalError = MutableStateFlow<String?>(null)
    val savingsGoalError: StateFlow<String?> = _savingsGoalError

    private val _userAlreadySetIncomeAndOutcomeData = MutableStateFlow<Boolean?>(null)
    val userAlreadySetIncomeAndOutcomeData: StateFlow<Boolean?> = _userAlreadySetIncomeAndOutcomeData

    private val exceptionHandler =
        CoroutineExceptionHandler { _, e ->
            println("Error, $e")
        }

    private fun getEmptyStringIfNull(double: Double?): String = if (double.toString() == "null") "" else double.toString()

    private fun updateFromDbCurrentUserInputs() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val user = userRepository.getUser()?.let { mapUserEntityToUserModel(it) }
            _yearlyNetIncome.value = getEmptyStringIfNull(user?.yearlyNetIncome)
            _savingsGoal.value = getEmptyStringIfNull(user?.savingsGoal)
            _recurrentSpendings.value = getEmptyStringIfNull(user?.recurrentSpendings)
            _userAlreadySetIncomeAndOutcomeData.value =
                user?.yearlyNetIncome != null &&
                user.savingsGoal != null &&
                user.recurrentSpendings != null
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val user = userRepository.getUser()
            if (user == null) {
                userRepository.createDefaultUser()
            }
            updateFromDbCurrentUserInputs()
        }
    }

    fun onYearlyNetIncomeChange(newValue: String) {
        _yearlyNetIncome.value = newValue
        _yearlyNetIncomeError.value =
            if (newValue.toDoubleOrNull() == null) {
                "This field can't be empty"
            } else {
                null
            }
    }

    fun onRecurrentSpendingsChange(newValue: String) {
        _recurrentSpendings.value = newValue
        _recurrentSpendingsError.value =
            if (newValue.toDoubleOrNull() == null) {
                "This field can't be empty"
            } else {
                null
            }
    }

    fun onSavingsGoalChange(newValue: String) {
        _savingsGoal.value = newValue
        _savingsGoalError.value =
            if (newValue.toDoubleOrNull() == null) {
                "This field can't be empty"
            } else {
                null
            }
    }

    private fun isInputFieldsFilledCorrectly(): Boolean =
        _yearlyNetIncomeError.value.isNullOrEmpty() &&
            _recurrentSpendingsError.value.isNullOrEmpty() &&
            _savingsGoalError.value.isNullOrEmpty()

    fun onSubmit(setData: () -> Unit) {
        if (isInputFieldsFilledCorrectly()) {
            updateUserIncomeAndOutcomeData(
                yearlyNetIncome = _yearlyNetIncome.value.toDoubleOrNull(),
                savingsGoal = _savingsGoal.value.toDoubleOrNull(),
                recurrentSpendings = _recurrentSpendings.value.toDoubleOrNull(),
                dailyBalance = getDailyAvailableMoneyAmount()
            )
            setData()
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

    private fun String.toDoubleOrZero(): Double {
        return try {
            this.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    private fun getDailyAvailableMoneyAmount(): Double {
        val income = _yearlyNetIncome.value.toDoubleOrZero()
        val spendings =  _recurrentSpendings.value.toDoubleOrZero()
        val savings = _savingsGoal.value.toDoubleOrZero()
        val daysInYear = getDaysInCurrentYear().takeIf { it > 0 } ?: return 0.0

        println("income - $income, savings -$savings, spendings - $spendings, daysInYear - $daysInYear")

        return ((income - spendings - savings) / daysInYear)
    }

    private fun updateUserIncomeAndOutcomeData(
        yearlyNetIncome: Double?,
        savingsGoal: Double?,
        recurrentSpendings: Double?,
        dailyBalance: Double
    ) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            userRepository.updateUserData(yearlyNetIncome, savingsGoal, recurrentSpendings, dailyBalance)
        }
    }
}
