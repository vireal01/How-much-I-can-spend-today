package com.vireal.hmicst.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "categories")
data class CategoryEntity
    @OptIn(ExperimentalUuidApi::class)
    constructor(
        @PrimaryKey val id: String = Uuid.random().toString(),
        val title: String,
        val imageUrl: String?,
    )
