package com.alcinacarlos.apitareasui.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alcinacarlos.apitareasui.data.model.Direccion
import com.alcinacarlos.apitareasui.dto.UsuarioRegisterDTO
import com.alcinacarlos.apitareasui.ui.viewmodel.AuthViewModel
/**
 * Representa la pantalla de registro de usuario.
 *
 * @param viewModel ViewModel encargado de la autenticación.
 * @param navController Controlador de navegación para cambiar de pantalla.
 */
@Composable
fun RegisterScreen(viewModel: AuthViewModel, navController: NavController) {
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

    // Estados para los mensajes de error
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordRepeatError by remember { mutableStateOf<String?>(null) }

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
                CampoRegister(value = username, label = "Usuario", onValueChange = { username = it }, error = usernameError)
                CampoRegister(value = email, label = "Email", onValueChange = { email = it }, error = emailError)
                CampoRegister(value = password, label = "Contraseña", onValueChange = { password = it }, isPassword = true, error = passwordError)
                CampoRegister(value = passwordRepeat, label = "Repetir Contraseña", onValueChange = { passwordRepeat = it }, isPassword = true, error = passwordRepeatError)
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
                        // Validar los datos
                        val isValid = validateData(
                            username, email, password, passwordRepeat,
                            onUsernameError = { usernameError = it },
                            onEmailError = { emailError = it },
                            onPasswordError = { passwordError = it },
                            onPasswordRepeatError = { passwordRepeatError = it }
                        )
                        if (isValid) {
                            viewModel.register(
                                UsuarioRegisterDTO(username, email, password, passwordRepeat, "USER", Direccion(calle, num, municipio, provincia, cp))
                            )
                        }
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
/**
 * Campo de entrada en el formulario de registro.
 *
 * @param value Valor del campo.
 * @param label Etiqueta del campo.
 * @param onValueChange Función que se ejecuta cuando el valor cambia.
 * @param isPassword Indica si el campo es de contraseña.
 * @param error Mensaje de error opcional.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoRegister(value: String, label: String, onValueChange: (String) -> Unit, isPassword: Boolean = false, error: String? = null) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (error != null) Color.Red else Color(0xFF3F51B5),
                cursorColor = if (error != null) Color.Red else Color(0xFF3F51B5),
                errorBorderColor = Color.Red
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            isError = error != null
        )
        if (error != null) {
            Text(text = error, color = Color.Red, style = TextStyle(fontSize = 12.sp))
        }
    }
}
/**
 * Valida los datos del formulario de registro
 *
 * @return `true` si los datos son válidos, `false` en caso contrario.
 */
fun validateData(
    username: String, email: String, password: String, passwordRepeat: String,
    onUsernameError: (String?) -> Unit, onEmailError: (String?) -> Unit,
    onPasswordError: (String?) -> Unit, onPasswordRepeatError: (String?) -> Unit
): Boolean {
    var isValid = true

    if (username.isBlank()) {
        onUsernameError("El usuario no puede estar vacío")
        isValid = false
    } else {
        onUsernameError(null)
    }

    if (email.isBlank()) {
        onEmailError("El email no puede estar vacío")
        isValid = false
    } else {
        onEmailError(null)
    }

    if (password.isBlank()) {
        onPasswordError("La contraseña no puede estar vacía")
        isValid = false
    } else {
        onPasswordError(null)
    }

    if (passwordRepeat.isBlank()) {
        onPasswordRepeatError("Repetir contraseña no puede estar vacío")
        isValid = false
    } else if (password != passwordRepeat) {
        onPasswordRepeatError("Las contraseñas no coinciden")
        isValid = false
    } else {
        onPasswordRepeatError(null)
    }

    return isValid
}