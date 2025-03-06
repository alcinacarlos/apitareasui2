package com.alcinacarlos.apitareasui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.alcinacarlos.apitareasui.navigation.AppNavigation
import com.alcinacarlos.apitareasui.ui.theme.ApitareasuiTheme
import com.alcinacarlos.apitareasui.ui.viewmodel.AuthViewModel
import com.alcinacarlos.apitareasui.data.remote.RetrofitInstance
import com.alcinacarlos.apitareasui.ui.viewmodel.TaskViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApitareasuiTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val authViewModel = AuthViewModel()
    val tareaViewModel = TaskViewModel(authViewModel)

    AppNavigation(navController, authViewModel, tareaViewModel)

}

