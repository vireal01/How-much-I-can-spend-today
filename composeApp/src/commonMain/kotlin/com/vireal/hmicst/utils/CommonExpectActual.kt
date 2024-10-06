package com.vireal.hmicst.utils

import androidx.room.RoomDatabase
import com.vireal.hmicst.data.database.AppDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

expect fun randomUUIDStr(): String
