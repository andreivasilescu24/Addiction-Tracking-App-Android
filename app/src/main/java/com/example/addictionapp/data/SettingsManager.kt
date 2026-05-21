package com.example.addictionapp.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    private val _themeColor = MutableStateFlow(prefs.getString("theme_color", "Blue") ?: "Blue")
    val themeColor: StateFlow<String> = _themeColor

    private val _language = MutableStateFlow(prefs.getString("language", "English") ?: "English")
    val language: StateFlow<String> = _language

    suspend fun setThemeColor(color: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString("theme_color", color).apply()
        _themeColor.value = color
    }

    suspend fun setLanguage(lang: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString("language", lang).apply()
        _language.value = lang
    }
}
