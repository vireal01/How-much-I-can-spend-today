package com.vireal.hmicst.utils

import android.app.Activity
import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vireal.hmicst.data.database.AppDatabase
import org.koin.mp.KoinPlatform
import java.util.UUID

private var activityProvider: () -> Activity = {
    throw IllegalArgumentException("Error")
}

fun setActivityProvider(provider: () -> Activity) {
    activityProvider = provider
}

actual fun randomUUIDStr(): String = UUID.randomUUID().toString()

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = KoinPlatform.getKoin().get<Application>()
    val dbFile = appContext.getDatabasePath(DB_Name)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
