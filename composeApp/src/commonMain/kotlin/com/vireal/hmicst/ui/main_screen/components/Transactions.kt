package com.vireal.hmicst.ui.main_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.ui.Paddings
import com.vireal.hmicst.ui.common.TransactionCell

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Transactions(
    onCreateStubTransactionBtnClick: () -> Unit,
    onISpentNothingOnTheDateClicked: () -> Unit,
    transactions: State<List<TransactionModel>>,
    totalSpentToday: State<Double>,
    isSelectedDateInited: State<Boolean>,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!isSelectedDateInited.value) {
            Text(
                text = "Looks like there are no transactions foe the date",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Button(onClick = { onISpentNothingOnTheDateClicked() }) {
                Text(
                    text = "I Spent nothing on the date :)",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        println("transactions.value.size =  ${transactions.value.size}")
        Button(onClick = { onCreateStubTransactionBtnClick() }) {
            Text(
                text = "+",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (transactions.value.isEmpty()) {
            Placeholder()
        } else {
            Text(text = "Spent today: ${totalSpentToday.value}")
            LazyColumn(
                contentPadding = PaddingValues(Paddings.one),
                verticalArrangement = Arrangement.spacedBy(Paddings.half),
                modifier = Modifier.padding(top = Paddings.half),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(transactions.value, key = { transaction -> transaction.id ?: 0 }) { transaction ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        TransactionCell(
                            transactionModel = transaction,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Placeholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No transactions yet!",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
