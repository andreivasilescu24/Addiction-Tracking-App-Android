package com.example.addictionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.data.model.Addiction
import com.data.model.AddictionCategory
import com.example.addictionapp.ui.viewmodel.AddictionViewModel
import com.util.AddictionUtils
import com.util.Translations
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackerScreen(
    viewModel: AddictionViewModel,
    language: String,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(AddictionCategory.OTHER) }
    var targetDays by remember { mutableIntStateOf(30) }
    var startDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startDateMillis)

    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Translations.getString("add_title", language)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(Translations.getString("add_name_label", language)) },
                placeholder = { Text(Translations.getString("add_name_placeholder", language)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Category Selection
            Column {
                Text(Translations.getString("add_category", language), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AddictionCategory.entries.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { 
                                val translationKey = "cat_${category.name.lowercase()}"
                                Text(Translations.getString(translationKey, language)) 
                            }
                        )
                    }
                }
            }

            // Target Days Selection
            Column {
                Text(Translations.getString("add_goal", language), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                val targets = listOf(7, 14, 30, 90, 365)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    targets.forEach { days ->
                        FilterChip(
                            selected = targetDays == days,
                            onClick = { targetDays = days },
                            label = { Text("${days}${Translations.getString("days_short", language)}") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Start Date Selection
            Column {
                Text(Translations.getString("add_start_date", language), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedCard(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(dateFormatter.format(Date(startDateMillis)))
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val sanitizedName = AddictionUtils.sanitizeInput(name)
                        viewModel.addAddiction(
                            Addiction(
                                name = sanitizedName,
                                category = selectedCategory,
                                targetDays = targetDays,
                                startDateMillis = startDateMillis
                            )
                        )
                        onSaveSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = name.isNotBlank()
            ) {
                Text(Translations.getString("add_btn", language))
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startDateMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    showDatePicker = false
                }) {
                    Text(Translations.getString("dialog_ok", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(Translations.getString("dialog_cancel", language))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
