package com.alcinacarlos.apitareasui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alcinacarlos.apitareasui.ui.viewmodel.AuthViewModel
import com.alcinacarlos.apitareasui.ui.view.LoginScreen
import com.alcinacarlos.apitareasui.ui.view.RegisterScreen
import com.alcinacarlos.apitareasui.ui.view.TasksScreen
import com.alcinacarlos.apitareasui.ui.view.WelcomeScreen
import com.alcinacarlos.apitareasui.ui.viewmodel.TaskViewModel

/**
 * Define la navegación de la aplicación mediante un NavHost.
 *
 * @param navController Controlador de navegación utilizado para gestionar las pantallas.
 * @param authViewModel ViewModel encargado de la autenticación del usuario.
 * @param taskViewModel ViewModel encargado de la gestión de las tareas.
 */
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
