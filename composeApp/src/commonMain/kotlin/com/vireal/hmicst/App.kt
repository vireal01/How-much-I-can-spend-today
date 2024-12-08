package com.vireal.hmicst

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vireal.hmicst.theme.HMICSTTheme
import com.vireal.hmicst.ui.main_screen.MainScreenViewModel
import com.vireal.hmicst.ui.navigation.Screens
import com.vireal.hmicst.ui.navigation.income_outcome_settings_flow.userIncomeAndOutcomeSettings
import com.vireal.hmicst.ui.navigation.main_flow.mainScreen
import com.vireal.hmicst.ui.user_income_and_outcome.UserStateViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        val userStateViewModel = koinViewModel<UserStateViewModel>()
        val mainScreenViewModel = koinViewModel<MainScreenViewModel>()

        var isLoading by remember { mutableStateOf(true) }
        val isUserAlreadySetIncomeAndOutcomeData =
            userStateViewModel.userAlreadySetIncomeAndOutcomeData.collectAsState(initial = null).value

        LaunchedEffect(isUserAlreadySetIncomeAndOutcomeData) {
            if (isUserAlreadySetIncomeAndOutcomeData != null) {
                isLoading = false
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            val startDestination =
                if (isUserAlreadySetIncomeAndOutcomeData == true) Screens.MainAppContent else Screens.UserIncomeAndOutcomeSettings
            val navController = rememberNavController()

            HMICSTTheme {
                HMICSTHost(
                    navController = navController,
                    startDestination = startDestination,
                    userStateViewModel = userStateViewModel,
                    mainScreenViewModel = mainScreenViewModel,
                )
            }
        }
    }
}

@Composable
fun HMICSTHost(
    navController: NavHostController,
    startDestination: Screens,
    userStateViewModel: UserStateViewModel,
    mainScreenViewModel: MainScreenViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        userIncomeAndOutcomeSettings(navController, userStateViewModel)
        mainScreen(navController, mainScreenViewModel)
    }
}
