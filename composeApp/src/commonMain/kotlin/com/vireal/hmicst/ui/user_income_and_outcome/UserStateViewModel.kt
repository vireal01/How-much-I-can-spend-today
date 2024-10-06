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

    private fun updateFromDbCurrentUserInputs() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val user = userRepository.getUser()?.let { mapUserEntityToUserModel(it) }
            _yearlyNetIncome.value = user?.yearlyNetIncome.toString()
            _savingsGoal.value = user?.savingsGoal.toString()
            _recurrentSpendings.value = user?.recurrentSpendings.toString()
            if (user?.yearlyNetIncome != null && user.savingsGoal != null && user.recurrentSpendings != null) {
                _userAlreadySetIncomeAndOutcomeData.value = true
            }
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
            )
            setData()
        }
    }

    private fun updateUserIncomeAndOutcomeData(
        yearlyNetIncome: Double?,
        savingsGoal: Double?,
        recurrentSpendings: Double?,
    ) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            userRepository.updateUserData(yearlyNetIncome, savingsGoal, recurrentSpendings)
        }
    }
}
