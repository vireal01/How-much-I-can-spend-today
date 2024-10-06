package com.vireal.hmicst.di

import com.vireal.hmicst.ui.main_screen.MainScreenViewModel
import com.vireal.hmicst.ui.user_income_and_outcome.UserStateViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::MainScreenViewModel)
        viewModelOf(::UserStateViewModel)
    }
