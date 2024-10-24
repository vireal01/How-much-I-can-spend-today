package com.vireal.hmicst.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.vireal.hmicst.data.database.dao.TransactionDao
import com.vireal.hmicst.data.database.dao.UserDao
import com.vireal.hmicst.data.database.entities.CategoryEntity
import com.vireal.hmicst.data.database.entities.TransactionEntity
import com.vireal.hmicst.data.database.entities.UserEntity

@Database(entities = [UserEntity::class, TransactionEntity::class, CategoryEntity::class], version = 1)
@TypeConverters(DatabaseTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase :
    RoomDatabase(),
    DB {
    abstract fun getTransactionDao(): TransactionDao

    abstract fun getUserDao(): UserDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

interface DB {
    fun clearAllTables() {}
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
