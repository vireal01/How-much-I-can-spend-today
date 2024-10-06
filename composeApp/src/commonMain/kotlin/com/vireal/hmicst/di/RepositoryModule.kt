package com.vireal.hmicst.di

import com.vireal.hmicst.data.database.AppDatabase
import com.vireal.hmicst.data.repository.TransactionRepository
import com.vireal.hmicst.data.repository.UserRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single {
            TransactionRepository(get<AppDatabase>().getTransactionDao())
        }

        single {
            UserRepository(get<AppDatabase>().getUserDao())
        }
    }
