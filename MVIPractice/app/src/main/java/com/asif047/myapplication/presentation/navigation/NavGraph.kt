package com.asif047.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asif047.myapplication.presentation.login.LoginScreen
import com.asif047.myapplication.presentation.login.LoginViewModel
import com.asif047.myapplication.presentation.user.UserScreen
import com.asif047.myapplication.presentation.user.UserViewModel

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object User : Screen("user")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.User.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.User.route) {
            val viewModel: UserViewModel = hiltViewModel()
            UserScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.User.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
