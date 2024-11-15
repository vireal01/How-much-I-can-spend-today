package com.vireal.hmicst.ui.models

import com.vireal.hmicst.data.models.TransactionModel

object StubTransaction {
    val busTicket =
        TransactionModel(
            title = "Bus ticket",
            amount = -3.0,
            categoryId = "Transport",
        )

    val musicItem =
        TransactionModel(
            title = "Something else",
            amount = -35.0,
            categoryId = "Music related",
        )

    val incomeTransaction =
        TransactionModel(
            title = "Refund",
            amount = 35.0,
            categoryId = "Music related",
        )
}
