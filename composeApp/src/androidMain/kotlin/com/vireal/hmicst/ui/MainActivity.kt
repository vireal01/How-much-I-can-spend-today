package com.vireal.hmicst.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.vireal.hmicst.App
import com.vireal.hmicst.utils.setActivityProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityProvider { this }
        setContent {
            enableEdgeToEdge(
                SystemBarStyle.dark(MaterialTheme.colorScheme.onSurface.toArgb()),
                SystemBarStyle.dark(MaterialTheme.colorScheme.onSurface.toArgb()),
            )
            App()
        }
    }
}
