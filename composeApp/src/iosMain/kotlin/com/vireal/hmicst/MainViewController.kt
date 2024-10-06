package com.vireal.hmicst

import androidx.compose.ui.window.ComposeUIViewController
import com.vireal.hmicst.di.initKoin

fun MainViewController() =
    ComposeUIViewController(
        configure = {
            initKoin()
        },
    ) {
        App()
    }
