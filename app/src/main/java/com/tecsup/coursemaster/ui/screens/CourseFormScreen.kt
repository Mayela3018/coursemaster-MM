package com.tecsup.coursemaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    var status by remember { mutableStateOf(courseToEdit?.status ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (courseToEdit == null) "Nuevo curso" else "Editar curso") },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración (horas)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val course = Course(
                        id = courseToEdit?.id ?: "",
                        name = name,
                        category = category,
                        duration = duration.toIntOrNull() ?: 0,
                        status = status,
                        userId = userId
                    )

                    // Guardar o actualizar
                    if (courseToEdit == null) {
                        viewModel.addCourse(course)
                    } else {
                        viewModel.updateCourse(course)
                    }

                    onFinish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text("Guardar")
            }
        }
    }
}
