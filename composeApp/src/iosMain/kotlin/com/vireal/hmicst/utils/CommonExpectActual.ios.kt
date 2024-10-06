package com.vireal.hmicst.utils

import androidx.room.Room
import androidx.room.RoomDatabase
import com.vireal.hmicst.data.database.AppDatabase
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSUUID

actual fun randomUUIDStr(): String = NSUUID().UUIDString()

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/$DB_Name"

    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}
