package com.vireal.hmicst.utils

import androidx.room.Room
import androidx.room.RoomDatabase
import com.vireal.hmicst.data.database.AppDatabase
import java.io.File
import java.util.UUID

actual fun randomUUIDStr(): String = UUID.randomUUID().toString()

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DB_Name)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
