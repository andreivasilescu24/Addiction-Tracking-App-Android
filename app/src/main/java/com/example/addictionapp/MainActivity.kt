package com.example.addictionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.addictionapp.ui.navigation.Screen
import com.example.addictionapp.ui.screens.AddTrackerScreen
import com.example.addictionapp.ui.screens.DashboardScreen
import com.example.addictionapp.ui.screens.GoalsScreen
import com.example.addictionapp.ui.screens.HistoryScreen
import com.example.addictionapp.ui.screens.SettingsScreen
import com.example.addictionapp.ui.screens.TrackerDetailScreen
import com.example.addictionapp.ui.theme.AddictionAppTheme
import com.example.addictionapp.ui.viewmodel.AddictionViewModel
import com.example.addictionapp.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val themeColor by settingsViewModel.themeColor.collectAsState()
            val language by settingsViewModel.language.collectAsState()
            
            AddictionAppTheme(themeColorName = themeColor) {
                AddictionApp(settingsViewModel, language)
            }
        }
    }
}

@Composable
fun AddictionApp(settingsViewModel: SettingsViewModel, language: String) {
    val navController = rememberNavController()
    val viewModel: AddictionViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                language = language,
                onAddTrackerClick = {
                    navController.navigate(Screen.AddTracker.route)
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                onGoalsClick = {
                    navController.navigate(Screen.Goals.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onTrackerClick = { id ->
                    navController.navigate(Screen.Details.createRoute(id))
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = viewModel,
                language = language,
                onHomeClick = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onGoalsClick = {
                    navController.navigate(Screen.Goals.route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }
        
        composable(Screen.Goals.route) {
            GoalsScreen(
                viewModel = viewModel,
                language = language,
                onHomeClick = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }
        
        composable(Screen.AddTracker.route) {
            AddTrackerScreen(
                viewModel = viewModel,
                language = language,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("addictionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val addictionId = backStackEntry.arguments?.getLong("addictionId") ?: 0L
            TrackerDetailScreen(
                addictionId = addictionId,
                viewModel = viewModel,
                language = language,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
