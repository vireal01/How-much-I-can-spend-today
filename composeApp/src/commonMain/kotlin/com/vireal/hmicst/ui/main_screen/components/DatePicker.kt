package com.vireal.hmicst.ui.main_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate

@Composable
fun DatePicker(
    onSelectPreviousDayClicked: () -> Unit,
    onSelectNextDayClicked: () -> Unit,
    selectedDate: LocalDate,
) {
    Row {
        Text(
            text = "<<",
            modifier =
                Modifier.padding(16.dp).clickable {
                    onSelectPreviousDayClicked()
                },
            textAlign = TextAlign.Center,
            style = typography.bodyLarge,
        )
        Text(
            text =
                selectedDate.toString(),
            modifier =
                Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            style = typography.bodyLarge,
        )
        Text(
            text = ">>",
            modifier =
                Modifier.padding(16.dp).clickable {
                    onSelectNextDayClicked()
                },
            textAlign = TextAlign.Center,
            style = typography.bodyLarge,
        )
    }
}
