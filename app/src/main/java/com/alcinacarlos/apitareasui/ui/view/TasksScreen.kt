package com.alcinacarlos.apitareasui.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alcinacarlos.apitareasui.data.model.Tarea
import com.alcinacarlos.apitareasui.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(viewModel: TaskViewModel, navController: NavController) {
    val tareas by viewModel.tareas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Tareas", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.obtenerTareas() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar", tint = Color.Black)
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
                tareas.isEmpty() -> Text("No hay tareas disponibles", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                else -> LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
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
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tarea.titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = tarea.descripcion, style = MaterialTheme.typography.bodyMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = tarea.completada,
                        onCheckedChange = { viewModel.marcarComoHecha(tarea) }
                    )
                    Text(text = if (tarea.completada) "Completada" else "Pendiente")
                }
                IconButton(onClick = { viewModel.eliminarTarea(tarea._id!!) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea", tint = Color.Red)
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Nueva Tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.crearTarea(titulo, descripcion, usuario)
                    onDismiss()
                }
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
