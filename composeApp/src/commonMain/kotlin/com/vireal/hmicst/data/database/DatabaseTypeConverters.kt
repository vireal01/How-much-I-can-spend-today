package com.vireal.hmicst.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class DatabaseTypeConverters {
    @TypeConverter
    fun fromString(value: String): LocalDate = value.let { LocalDate.parse(it) }

    @TypeConverter
    fun dateToString(date: LocalDate): String = date.toString()
}
