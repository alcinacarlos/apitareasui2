package com.alcinacarlos.apitareasui.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alcinacarlos.apitareasui.ui.viewmodel.AuthViewModel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.TextStyle

/**
 * Pantalla de inicio de sesión.
 *
 * @param viewModel ViewModel de autenticación.
 * @param navController Controlador de navegación.
 */
@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val token by viewModel.token.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(token) {
        if (token.isNotBlank()) navController.navigate("tareas")
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Iniciar Sesión", fontSize = 24.sp, color = Color(0xFF3F51B5))
                Spacer(modifier = Modifier.height(24.dp))

                CampoUsuario(value = username, label = "Usuario", onValueChange = { username = it })
                CampoContrasenia(value = password, label = "Contraseña", onValueChange = { password = it })

                Spacer(modifier = Modifier.height(16.dp))
                if (loading) CircularProgressIndicator()
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Button(
                    onClick = { viewModel.login(username, password) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                ) {
                    Text("Ingresar", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No tienes cuenta? Regístrate",
                    modifier = Modifier
                        .clickable { navController.navigate("register") }
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color(0xFF3F51B5))
                )
            }
        }
    }
}

/**
 * Campo de entrada de contraseña con visibilidad alternable.
 *
 * @param value Valor actual de la contraseña.
 * @param label Etiqueta del campo.
 * @param onValueChange Función que se ejecuta cuando cambia el valor.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoContrasenia(value: String, label: String, onValueChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF3F51B5),
            cursorColor = Color(0xFF3F51B5)
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
            }
        }
    )
}

/**
 * Campo de entrada de texto para el usuario.
 *
 * @param value Valor actual del campo.
 * @param label Etiqueta del campo.
 * @param onValueChange Función que se ejecuta cuando cambia el valor.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoUsuario(value: String, label: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF3F51B5),
            cursorColor = Color(0xFF3F51B5)
        )
    )
}
