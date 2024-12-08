package com.vireal.hmicst.ui.main_screen.components.transactionCell

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.vireal.hmicst.data.models.TransactionModel
import com.vireal.hmicst.ui.Paddings
import com.vireal.hmicst.utils.IconSize
import kotlin.math.absoluteValue

@Composable
fun TransactionCell(
    transactionModel: TransactionModel,
    modifier: Modifier = Modifier,
    icon: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(Paddings.one),
                ).padding(vertical = Paddings.half, horizontal = Paddings.half),
    ) {
        icon()
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

        Amount(amount = transactionModel.amount)
    }
}

@Composable
fun Icon(
    painter: Painter,
    modifier: Modifier = Modifier,
    size: IconSize = IconSize.XS,
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier.size(size.size),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.material.Icon(
            painter = painter,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun Amount(
    modifier: Modifier = Modifier,
    amount: Double,
) {
    Box(
        modifier = modifier.then(Modifier.fillMaxHeight()),
        contentAlignment = Alignment.Center,
    ) {
        val textStyle =
            if (amount >= 0) {
                MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
            } else {
                MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.errorContainer)
            }

        val amountValue =
            if (amount.toInt().toDouble() == amount) {
                amount.absoluteValue.toInt().toString()
            } else {
                amount.absoluteValue.toString()
            }

        Text(
            text = "$amountValue$",
            style = textStyle,
            modifier = Modifier.fillMaxHeight().align(Alignment.Center),
        )
    }
}
