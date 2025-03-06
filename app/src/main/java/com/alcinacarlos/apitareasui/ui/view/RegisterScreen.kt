package com.alcinacarlos.apitareasui.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alcinacarlos.apitareasui.data.model.Direccion
import com.alcinacarlos.apitareasui.dto.UsuarioRegisterDTO
import com.alcinacarlos.apitareasui.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(viewModel: AuthViewModel = viewModel(), navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val registerError by viewModel.error.collectAsState()
    val registrationSuccess by viewModel.registrationSuccess.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            showDialog = true
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Registro Exitoso") },
            text = { Text("Te has registrado con éxito. Puedes iniciar sesión ahora") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navController.navigate("login")
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6)), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Registro", fontWeight = FontWeight.SemiBold, fontSize = 26.sp, color = Color(0xFF3F51B5))
                Spacer(modifier = Modifier.height(16.dp))
                CampoRegister(value = username, label = "Usuario", onValueChange = { username = it })
                CampoRegister(value = email, label = "Email", onValueChange = { email = it })
                CampoRegister(value = password, label = "Contraseña", onValueChange = { password = it }, isPassword = true)
                CampoRegister(value = passwordRepeat, label = "Repetir Contraseña", onValueChange = { passwordRepeat = it }, isPassword = true)
                CampoRegister(value = calle, label = "Calle", onValueChange = { calle = it })
                CampoRegister(value = num, label = "Número", onValueChange = { num = it })
                CampoRegister(value = municipio, label = "Municipio", onValueChange = { municipio = it })
                CampoRegister(value = provincia, label = "Provincia", onValueChange = { provincia = it })
                CampoRegister(value = cp, label = "Código Postal", onValueChange = { cp = it })

                Spacer(modifier = Modifier.height(16.dp))
                if (loading) CircularProgressIndicator()
                registerError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Button(
                    onClick = {
                        viewModel.register(
                            UsuarioRegisterDTO(username, email, password, passwordRepeat, "USER", Direccion(calle, num, municipio, provincia, cp))
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                ) {
                    Text("Registrarse", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ya tienes cuenta? Inicia sesión",
                    modifier = Modifier
                        .clickable { navController.navigate("login") }
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color(0xFF3F51B5))
                )

            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoRegister(value: String, label: String, onValueChange: (String) -> Unit, isPassword: Boolean = false) {
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
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}
