package com.vireal.hmicst.ui.navigation.income_outcome_settings_flow

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vireal.hmicst.ui.navigation.Screens
import com.vireal.hmicst.ui.user_income_and_outcome.UserIncomeAndOutcomeSettingsScreen
import com.vireal.hmicst.ui.user_income_and_outcome.UserStateViewModel

fun NavGraphBuilder.userIncomeAndOutcomeSettings(
    navController: NavController,
    userStateViewModel: UserStateViewModel,
) {
    composable(route = Screens.UserIncomeAndOutcomeSettings.route) {
        UserIncomeAndOutcomeSettingsScreen(
            viewModel = userStateViewModel,
            onSetDataClicked = {
                navController.navigate(Screens.MainAppContent.route) {
                    launchSingleTop = true
                    popUpTo(Screens.UserIncomeAndOutcomeSettings.route) {
                        inclusive = true
                    }
                }
            },
        )
    }
}
