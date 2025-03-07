package com.alcinacarlos.apitareasui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alcinacarlos.apitareasui.ui.viewmodel.AuthViewModel
import com.alcinacarlos.apitareasui.ui.view.LoginScreen
import com.alcinacarlos.apitareasui.ui.view.RegisterScreen
import com.alcinacarlos.apitareasui.ui.view.TasksScreen
import com.alcinacarlos.apitareasui.ui.view.WelcomeScreen
import com.alcinacarlos.apitareasui.ui.viewmodel.TaskViewModel

@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel, taskViewModel: TaskViewModel) {

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("login") {
            LoginScreen(authViewModel, navController)
        }
        composable("register") {
            RegisterScreen(authViewModel, navController)
        }
        composable("tareas") {
            TasksScreen(taskViewModel)
        }
    }
}