package com.vireal.hmicst.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import howmuchicanspendtoday.composeapp.generated.resources.Res
import howmuchicanspendtoday.composeapp.generated.resources.dark_mode
import howmuchicanspendtoday.composeapp.generated.resources.light_mode
import howmuchicanspendtoday.composeapp.generated.resources.system_default
import org.jetbrains.compose.resources.StringResource

const val DB_Name = "hmicst"

enum class Theme(
    val title: StringResource,
) {
    SYSTEM_DEFAULT(Res.string.system_default),
    LIGHT_MODE(Res.string.light_mode),
    DARK_MODE(Res.string.dark_mode),
}

enum class IconSize(
    val size: Dp,
) {
    XXS(
        size = 24.dp,
    ),
    XS(
        size = 32.dp,
    ),
    S(
        size = 40.dp,
    ),
    M(
        size = 56.dp,
    ),
    L(
        size = 56.dp,
    ),
}
