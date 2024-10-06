package com.vireal.hmicst

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "How much I can spend today",
        ) {
            App()
        }
    }
