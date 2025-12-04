package com.tecsup.coursemaster.navigation
//
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.coursemaster.auth.AuthViewModel
import com.tecsup.coursemaster.data.Course
import com.tecsup.coursemaster.data.CourseViewModel
import com.tecsup.coursemaster.ui.screens.CourseFormScreen
import com.tecsup.coursemaster.ui.screens.CourseListScreen
import com.tecsup.coursemaster.ui.screens.LoginScreen
import com.tecsup.coursemaster.ui.screens.RegisterScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel()
    val courseViewModel: CourseViewModel = viewModel()

    var userId by remember { mutableStateOf<String?>(null) }
    var courseToEdit by remember { mutableStateOf<Course?>(null) }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    userId = authViewModel.getCurrentUserId()
                    navController.navigate("courses") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    userId = authViewModel.getCurrentUserId()
                    navController.navigate("courses") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToLogin = { navController.popBackStack() }
            )
        }

        composable("courses") {
            val currentUser = userId ?: authViewModel.getCurrentUserId().orEmpty()

            CourseListScreen(
                viewModel = courseViewModel,
                userId = currentUser,
                onCreateCourse = {
                    courseToEdit = null
                    navController.navigate("courseForm")
                },
                onEditCourse = { course ->
                    courseToEdit = course
                    navController.navigate("courseForm")
                },
                onLogout = {
                    authViewModel.logout()
                    userId = null
                    navController.navigate("login") {
                        popUpTo("courses") { inclusive = true }
                    }
                }
            )
        }

        composable("courseForm") {
            val currentUser = userId ?: authViewModel.getCurrentUserId().orEmpty()

            CourseFormScreen(
                viewModel = courseViewModel,
                userId = currentUser,
                courseToEdit = courseToEdit,
                onFinish = {
                    navController.popBackStack()
                }
            )
        }
    }
}
