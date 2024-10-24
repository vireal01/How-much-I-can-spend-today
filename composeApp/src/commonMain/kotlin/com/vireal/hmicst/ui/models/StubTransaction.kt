package com.vireal.hmicst.ui.models

import com.vireal.hmicst.data.models.TransactionModel

object StubTransaction {
    val busTicket =
        TransactionModel(
            title = "Bus ticket",
            amount = 3.0,
            categoryId = "Transport",
        )

    val musicItem =
        TransactionModel(
            title = "Guitar",
            amount = 153.0,
            categoryId = "Music related",
        )
}
