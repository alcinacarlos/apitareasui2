package com.alcinacarlos.apitareasui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alcinacarlos.apitareasui.ui.viewmodel.AuthViewModel
import com.alcinacarlos.apitareasui.ui.view.LoginScreen
import com.alcinacarlos.apitareasui.ui.view.RegisterScreen

@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    val token = authViewModel.token.collectAsState().value

    NavHost(navController = navController, startDestination = if (token == null) "login" else "taskList") {
        composable("login") {
            LoginScreen(viewModel = authViewModel, navController) {
                navController.navigate("taskList") { popUpTo("login") { inclusive = true } }
            }
        }
        composable("register") {
            RegisterScreen(viewModel = authViewModel, navController) {
                navController.navigate("login")
            }
        }
    }
}