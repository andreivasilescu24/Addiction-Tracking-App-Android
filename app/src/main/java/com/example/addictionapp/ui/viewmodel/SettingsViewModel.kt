package com.example.addictionapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.addictionapp.data.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    val themeColor = settingsManager.themeColor
    val language = settingsManager.language

    fun setThemeColor(color: String) {
        settingsManager.setThemeColor(color)
    }

    fun setLanguage(lang: String) {
        settingsManager.setLanguage(lang)
    }
}
