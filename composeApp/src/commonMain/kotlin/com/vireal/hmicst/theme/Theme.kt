package com.vireal.hmicst.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.vireal.hmicst.utils.Theme

val DarkColorScheme =
    darkColorScheme(
        primary = primaryDark,
        secondary = secondaryDark,
        tertiary = tertiaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        onTertiary = onTertiaryDark,
        onTertiaryContainer = onTertiaryContainerDark,
        tertiaryContainer = tertiaryContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        outline = outlineDark,
    )

val LightColorScheme =
    lightColorScheme(
        primary = primaryLight,
        secondary = secondaryLight,
        tertiary = tertiaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        onTertiary = onTertiaryLight,
        onTertiaryContainer = onTertiaryContainerLight,
        tertiaryContainer = tertiaryContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        outline = outlineLight,
    )

@Composable
fun HMICSTTheme(
    appTheme: String? = Theme.DARK_MODE.name,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when (appTheme) {
            Theme.LIGHT_MODE.name -> {
                LightColorScheme
            }
            Theme.DARK_MODE.name -> {
                DarkColorScheme
            }
            else -> {
                if (darkTheme) {
                    DarkColorScheme
                } else {
                    LightColorScheme
                }
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = AppTypography,
    )
}
