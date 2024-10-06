package com.vireal.hmicst.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "transactions")
data class TransactionEntity
    @OptIn(ExperimentalUuidApi::class)
    constructor(
        @PrimaryKey val id: String = Uuid.random().toString(),
        val title: String?,
        val date: Long,
        val amount: Double,
        val categoryId: String,
        val userId: Long,
    )
