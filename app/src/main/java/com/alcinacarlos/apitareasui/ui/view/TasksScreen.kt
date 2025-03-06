package com.alcinacarlos.apitareasui.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alcinacarlos.apitareasui.data.model.Tarea
import com.alcinacarlos.apitareasui.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(viewModel: TaskViewModel) {
    val tareas by viewModel.tareas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tareas", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = { viewModel.obtenerTareas() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = Color.Black
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Tarea", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading -> CircularProgressIndicator()
                error != null -> Text(error!!, color = Color.Red)
                tareas.isEmpty() -> Text(
                    "No hay tareas disponibles",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )

                else -> LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)) {
                    items(tareas) { tarea -> TaskItem(tarea, viewModel) }
                }
            }
        }
    }

    if (showDialog) {
        AddTaskDialog(onDismiss = { showDialog = false }, viewModel = viewModel)
    }
}

@Composable
fun TaskItem(tarea: Tarea, viewModel: TaskViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        colors = if (!tarea.completada) CardDefaults.cardColors(containerColor = Color(0xFFB9D7D9)) else CardDefaults.cardColors(
            containerColor = Color(0xFFC4CECE)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(10.dp).widthIn(max = 250.dp)) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = tarea.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )

            }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = { viewModel.marcarComoHecha(tarea) }
                )
                IconButton(onClick = { viewModel.eliminarTarea(tarea._id!!) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar tarea",
                        tint = Color.Red
                    )
                }
            }
        }



    }
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, viewModel: TaskViewModel) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }

    var tituloError by remember { mutableStateOf(false) }
    var descripcionError by remember { mutableStateOf(false) }
    var usuarioError by remember { mutableStateOf(false) }

    val errorDiag by viewModel.errorDialog.collectAsState()
    var shouldDismiss by remember { mutableStateOf(false) }

    LaunchedEffect(errorDiag) {
        if (errorDiag.isNullOrBlank() && shouldDismiss) {
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Nueva Tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        tituloError = false
                    },
                    label = { Text("Título") },
                    isError = tituloError,
                    singleLine = true
                )
                if (tituloError) {
                    Text("El título no puede estar vacío", color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                        descripcionError = false
                    },
                    label = { Text("Descripción") },
                    isError = descripcionError,
                    singleLine = true
                )
                if (descripcionError) {
                    Text("La descripción no puede estar vacía", color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        usuarioError = false
                    },
                    label = { Text("Usuario") },
                    isError = usuarioError,
                    singleLine = true
                )
                if (!errorDiag.isNullOrBlank()) {
                    Text(errorDiag!!, color = Color.Red, fontSize = 12.sp)
                }

                if (usuarioError) {
                    Text("El usuario no puede estar vacío", color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val esValido = titulo.isNotBlank() && descripcion.isNotBlank() && usuario.isNotBlank()

                    if (esValido) {
                        viewModel.crearTarea(titulo, descripcion, usuario)
                        if (errorDiag.isNullOrBlank()) {
                            shouldDismiss = true
                        }

                    } else {
                        tituloError = titulo.isBlank()
                        descripcionError = descripcion.isBlank()
                        usuarioError = usuario.isBlank()
                    }
                }
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}


