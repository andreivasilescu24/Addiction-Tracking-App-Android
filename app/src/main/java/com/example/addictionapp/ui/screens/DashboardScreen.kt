package com.example.addictionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.addictionapp.ui.components.MotivationCard
import com.example.addictionapp.ui.components.TrackerCard
import com.example.addictionapp.ui.viewmodel.AddictionViewModel
import com.util.AddictionUtils
import com.util.Translations

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: AddictionViewModel,
    language: String,
    onAddTrackerClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onTrackerClick: (Long) -> Unit
) {
    val addictions by viewModel.allAddictions.collectAsState()
    val quote by viewModel.quote.collectAsState()
    val isQuoteLoading by viewModel.isQuoteLoading.collectAsState()
    
    val currentDate = remember { 
        SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date())
    }
    
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "On track", "Struggling")
    val translatedFilters = listOf(
        Translations.getString("filter_all", language),
        Translations.getString("filter_on_track", language),
        Translations.getString("filter_struggling", language)
    )

    val filteredAddictions = remember(addictions, selectedFilter) {
        when (selectedFilter) {
            "On track" -> addictions.filter { 
                val streak = AddictionUtils.calculateStreak(it.addiction.startDateMillis, it.relapses)
                streak >= 7 // Example: On track if streak is 7+ days
            }
            "Struggling" -> addictions.filter { 
                val streak = AddictionUtils.calculateStreak(it.addiction.startDateMillis, it.relapses)
                streak < 7
            }
            else -> addictions
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(Translations.getString("dashboard_title", language), style = MaterialTheme.typography.titleMedium)
                        Text(currentDate, style = MaterialTheme.typography.labelSmall)
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(Translations.getString("nav_home", language)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onHistoryClick,
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text(Translations.getString("nav_history", language)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onGoalsClick,
                    icon = { Icon(Icons.Default.Star, contentDescription = "Goals") },
                    label = { Text(Translations.getString("nav_goals", language)) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTrackerClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Tracker")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                MotivationCard(
                    quote = quote,
                    isLoading = isQuoteLoading,
                    onRefresh = { viewModel.fetchQuote() }
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                // Filter Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filters.size, key = { filters[it] }) { index ->
                        val filter = filters[index]
                        val translatedLabel = translatedFilters[index]
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(translatedLabel) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = Translations.getString("active_trackers", language),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (filteredAddictions.isEmpty()) {
                item(key = "empty_state") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (addictions.isEmpty()) Translations.getString("no_trackers", language) else Translations.getString("no_filters", language),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(
                    items = filteredAddictions,
                    key = { it.addiction.id }
                ) { addictionWithRelapses ->
                    TrackerCard(
                        addictionWithRelapses = addictionWithRelapses,
                        language = language,
                        onClick = { onTrackerClick(addictionWithRelapses.addiction.id) }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
