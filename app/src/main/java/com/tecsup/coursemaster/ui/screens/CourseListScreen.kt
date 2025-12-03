package com.tecsup.coursemaster.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.coursemaster.data.Course
import com.tecsup.coursemaster.data.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CourseListScreen(
    viewModel: CourseViewModel,
    userId: String,
    onCreateCourse: () -> Unit,
    onEditCourse: (Course) -> Unit,
    onLogout: () -> Unit
) {
    val courses by viewModel.ui.collectAsState()

    LaunchedEffect(userId) {
        viewModel.load(userId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis cursos") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Salir", color = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateCourse) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo curso")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(courses) { course ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text(course.name, style = MaterialTheme.typography.titleMedium)
                        Text("Categoría: ${course.category}")
                        Text("Duración: ${course.duration} h")
                        Text("Estado: ${course.status}")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { onEditCourse(course) }) {
                                Text("Editar")
                            }
                            TextButton(onClick = { viewModel.deleteCourse(course.id) }) {
                                Text("Eliminar", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}
