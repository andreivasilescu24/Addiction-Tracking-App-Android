package com.example.addictionapp.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object History : Screen("history")
    object Goals : Screen("goals")
    object AddTracker : Screen("add_tracker")
    object Settings : Screen("settings")
    object Details : Screen("details/{addictionId}") {
        fun createRoute(addictionId: Long) = "details/$addictionId"
    }
}
