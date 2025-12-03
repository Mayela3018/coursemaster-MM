package com.tecsup.coursemaster.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.coursemaster.data.Course
import com.tecsup.coursemaster.data.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFormScreen(
    viewModel: CourseViewModel,
    userId: String,
    courseToEdit: Course?,
    onFinish: () -> Unit
) {
    var name by remember { mutableStateOf(courseToEdit?.name ?: "") }
    var category by remember { mutableStateOf(courseToEdit?.category ?: "") }
    var duration by remember { mutableStateOf(courseToEdit?.duration?.toString() ?: "") }
    var status by remember { mutableStateOf(courseToEdit?.status ?: "Pendiente") }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showStatusMenu by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val categories = listOf(
        "Programación", "Diseño", "Marketing", "Negocios",
        "Idiomas", "Ciencias", "Arte", "Música", "Deportes", "Otros"
    )

    val statuses = listOf("Pendiente", "En progreso", "Completado", "Abandonado")

    val isFormValid = name.isNotBlank() &&
            category.isNotBlank() &&
            duration.isNotBlank() &&
            duration.toIntOrNull() != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (courseToEdit == null) "Nuevo Curso" else "Editar Curso",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Sección: Información básica
            SectionHeader(
                icon = Icons.Default.Info,
                title = "Información Básica"
            )

            // Campo: Nombre del curso
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del curso") },
                leadingIcon = { Icon(Icons.Default.Create, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && name.isBlank(),
                supportingText = if (showError && name.isBlank()) {
                    { Text("El nombre es obligatorio") }
                } else null
            )

            // Campo: Categoría (Dropdown)
            ExposedDropdownMenuBox(
                expanded = showCategoryMenu,
                onExpandedChange = { showCategoryMenu = it }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isError = showError && category.isBlank(),
                    supportingText = if (showError && category.isBlank()) {
                        { Text("Selecciona una categoría") }
                    } else null,
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                showCategoryMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (category == cat)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        )
                    }
                }
            }

            // Sección: Detalles del curso
            SectionHeader(
                icon = Icons.Default.Settings,
                title = "Detalles del Curso"
            )

            // Campo: Duración
            OutlinedTextField(
                value = duration,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        duration = it
                    }
                },
                label = { Text("Duración (horas)") },
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError && (duration.isBlank() || duration.toIntOrNull() == null),
                supportingText = if (showError && (duration.isBlank() || duration.toIntOrNull() == null)) {
                    { Text("Ingresa una duración válida") }
                } else {
                    { Text("Solo números") }
                }
            )

            // Campo: Estado (Dropdown)
            ExposedDropdownMenuBox(
                expanded = showStatusMenu,
                onExpandedChange = { showStatusMenu = it }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showStatusMenu)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = showStatusMenu,
                    onDismissRequest = { showStatusMenu = false }
                ) {
                    statuses.forEach { st ->
                        DropdownMenuItem(
                            text = { Text(st) },
                            onClick = {
                                status = st
                                showStatusMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (status == st)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Botón guardar
            Button(
                onClick = {
                    if (isFormValid) {
                        val course = Course(
                            id = courseToEdit?.id ?: "",
                            name = name,
                            category = category,
                            duration = duration.toIntOrNull() ?: 0,
                            status = status,
                            userId = userId
                        )

                        if (courseToEdit == null) {
                            viewModel.addCourse(course)
                        } else {
                            viewModel.updateCourse(course)
                        }
                        onFinish()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    if (courseToEdit == null) Icons.Default.Add else Icons.Default.Check,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (courseToEdit == null) "Crear Curso" else "Guardar Cambios",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Botón cancelar
            OutlinedButton(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}