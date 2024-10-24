package com.vireal.hmicst.ui.navigation.main_flow

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vireal.hmicst.ui.main_screen.MainScreen
import com.vireal.hmicst.ui.main_screen.MainScreenViewModel
import com.vireal.hmicst.ui.navigation.Screens

fun NavGraphBuilder.mainScreen(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel,
) {
    composable(route = Screens.MainAppContent.route) {
        MainScreen(
            navController = navController,
            viewModel = mainScreenViewModel,
            onSettingsClicked = {
                navController.navigate(Screens.UserIncomeAndOutcomeSettings.route) {
                    launchSingleTop = true
                    popUpTo(Screens.MainAppContent.route) {
                        inclusive = true
                    }
                }
            },
        )
    }
}
