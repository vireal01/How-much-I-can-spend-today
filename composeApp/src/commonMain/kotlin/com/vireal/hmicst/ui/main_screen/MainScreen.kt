package com.vireal.hmicst.ui.main_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vireal.hmicst.ui.main_screen.components.DatePicker
import com.vireal.hmicst.ui.main_screen.components.RestAmountChart
import com.vireal.hmicst.ui.main_screen.components.Transactions

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainScreenViewModel,
    onSettingsClicked: () -> Unit,
) {
    Content(viewModel, onSettingsClicked)
}

@Composable
fun Content(
    viewModel: MainScreenViewModel,
    onSettingsClicked: () -> Unit,
) {
    val selectedDate = viewModel.selectedDate.collectAsState()
    val transactions = viewModel.allTransactionsForSelectedDay.collectAsState()
    val totalSpentToday = viewModel.totalSpentForSelectedDay.collectAsState()

    LaunchedEffect(true) {
        viewModel.init()
    }
    Column(
        modifier =
            Modifier
                .padding(30.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .clip(shape = RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Text(
                text = "Recalculate",
                modifier =
                    Modifier.padding(16.dp).clickable {
                        onSettingsClicked()
                    },
                textAlign = TextAlign.Center,
                style = typography.bodyLarge,
            )
        }

        Spacer(modifier = Modifier.width(48.dp))
        DatePicker(
            onSelectNextDayClicked = viewModel::selectNextDay,
            onSelectPreviousDayClicked = viewModel::selectPreviousDay,
            selectedDate = selectedDate.value,
            onDateClicked = viewModel::selectCurrentDay,
        )
        Spacer(modifier = Modifier.width(48.dp))

        RestAmountChart(viewModel)

        Spacer(modifier = Modifier.width(48.dp))
        Transactions(
            onCreateStubTransactionBtnClick = viewModel::createDummyTransactionForSelectedDay,
            transactions = transactions,
            totalSpentToday = totalSpentToday,
        )
    }
}
