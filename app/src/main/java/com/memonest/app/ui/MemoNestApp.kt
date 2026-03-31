package com.memonest.app.ui

import android.app.Application

class MemoNestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemePreferenceManager(this).applySavedTheme()
    }
}
