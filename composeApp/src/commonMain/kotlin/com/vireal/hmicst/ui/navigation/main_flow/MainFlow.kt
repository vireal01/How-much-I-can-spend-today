package com.vireal.hmicst.ui.navigation.main_flow

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vireal.hmicst.ui.main_screen.MainScreen
import com.vireal.hmicst.ui.navigation.Screens

fun NavGraphBuilder.mainScreen(navController: NavController) {
    composable(route = Screens.MainAppContent.route) {
        MainScreen(navController)
    }
}
