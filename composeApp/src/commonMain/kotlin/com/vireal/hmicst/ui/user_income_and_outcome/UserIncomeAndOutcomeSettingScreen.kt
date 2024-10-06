package com.vireal.hmicst.ui.user_income_and_outcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vireal.hmicst.ui.common.OutlinedTopAlignedTextField

@Composable
fun UserIncomeAndOutcomeSettingsScreen(
    viewModel: UserStateViewModel,
    onSetDataClicked: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier =
                    Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp), // Отступы между элементами
            ) {
                Content(viewModel, onSetDataClicked)
            }
        },
    )
}

@Composable
fun Content(
    viewModel: UserStateViewModel,
    onSetDataClicked: () -> Unit,
) {
    val yearlyNetIncome by viewModel.yearlyNetIncome.collectAsState()
    val recurrentSpendings by viewModel.recurrentSpendings.collectAsState()
    val savingsGoal by viewModel.savingsGoal.collectAsState()

    val yearlyNetIncomeError by viewModel.yearlyNetIncomeError.collectAsState()
    val recurrentSpendingsError by viewModel.recurrentSpendingsError.collectAsState()
    val savingsGoalError by viewModel.savingsGoalError.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTopAlignedTextField(
            value = yearlyNetIncome,
            onValueChange = viewModel::onYearlyNetIncomeChange,
            label = "Yearly Net Income",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = yearlyNetIncomeError != null,
        )

        OutlinedTopAlignedTextField(
            value = recurrentSpendings,
            onValueChange = viewModel::onRecurrentSpendingsChange,
            label = "Recurrent Spendings",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = recurrentSpendingsError != null,
        )

        OutlinedTopAlignedTextField(
            value = savingsGoal,
            onValueChange = viewModel::onSavingsGoalChange,
            label = "Savings Goal",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = savingsGoalError != null,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                onClick = {
                    viewModel.onSubmit { onSetDataClicked() }
                },
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Text("Submit")
            }
        }
    }
}
