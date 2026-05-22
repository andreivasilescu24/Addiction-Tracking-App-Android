package com.example.addictionapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictionapp.data.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    val themeColor = settingsManager.themeColor
    val language = settingsManager.language

    fun setThemeColor(color: String) {
        viewModelScope.launch {
            settingsManager.setThemeColor(color)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            settingsManager.setLanguage(lang)
        }
    }
}
