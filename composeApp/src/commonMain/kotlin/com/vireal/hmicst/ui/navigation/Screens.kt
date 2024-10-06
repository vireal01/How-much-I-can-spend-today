package com.vireal.hmicst.ui.navigation

import androidx.navigation.NamedNavArgument

sealed class Screens(
    val route: String,
    val navArgument: List<NamedNavArgument> = emptyList(),
) {
    data object UserIncomeAndOutcomeSettings : Screens("user_income_and_outcome_settings")

    data object MainAppContent : Screens("main_app_content")
}
