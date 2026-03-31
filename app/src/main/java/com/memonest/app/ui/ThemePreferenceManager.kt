package com.memonest.app.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class ThemePreferenceManager(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun applySavedTheme() {
        AppCompatDelegate.setDefaultNightMode(currentThemeMode())
    }

    fun currentThemeMode(): Int {
        return preferences.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun setThemeMode(mode: Int) {
        preferences.edit()
            .putInt(KEY_THEME_MODE, mode)
            .apply()
        applySavedTheme()
    }

    companion object {
        private const val PREFS_NAME = "memonest_theme"
        private const val KEY_THEME_MODE = "theme_mode"
    }
}
