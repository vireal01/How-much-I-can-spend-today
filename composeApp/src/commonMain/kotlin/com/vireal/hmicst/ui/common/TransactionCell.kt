package com.vireal.hmicst.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vireal.hmicst.data.models.TransactionModel

@Composable
fun TransactionCell(
    transactionModel: TransactionModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
        ) {
            Text(
                text = transactionModel.title.toString(),
                style = MaterialTheme.typography.labelMedium,
            )

            Text(
                text = transactionModel.categoryId,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
            )
        }

        Text(
            text = "${transactionModel.amount.toInt()}$",
            style = MaterialTheme.typography.titleMedium.copy(color = Color.DarkGray),
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}
