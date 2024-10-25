package com.vireal.hmicst.ui.main_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vireal.hmicst.ui.main_screen.MainScreenViewModel

@Composable
fun RestAmountChart(viewModel: MainScreenViewModel) {
    Column(
        modifier =
            Modifier
                .padding(30.dp)
                .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Remaining for today")

        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text =
                viewModel.dailyAvailableMoneyAmount
                    .collectAsState()
                    .value
                    .toString(),
        )
    }
}
