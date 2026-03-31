package com.memonest.app.auth

import android.content.Context
import android.provider.Settings

class UserSessionManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val appContext = context.applicationContext

    fun currentSession(): UserSession {
        val ownerId = prefs.getString(KEY_OWNER_ID, null)
        val displayName = prefs.getString(KEY_DISPLAY_NAME, null)
        val email = prefs.getString(KEY_EMAIL, null)
        val isGuest = prefs.getBoolean(KEY_IS_GUEST, true)

        if (ownerId != null && displayName != null) {
            return UserSession(ownerId, displayName, email, isGuest)
        }

        val fallbackId = "guest-" + (Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
            ?: System.currentTimeMillis().toString())
        val session = UserSession(ownerId = fallbackId, displayName = "My Journal", isGuest = true)
        save(session)
        return session
    }

    fun save(session: UserSession) {
        prefs.edit()
            .putString(KEY_OWNER_ID, session.ownerId)
            .putString(KEY_DISPLAY_NAME, session.displayName)
            .putString(KEY_EMAIL, session.email)
            .putBoolean(KEY_IS_GUEST, session.isGuest)
            .apply()
    }

    fun signOutToGuest() {
        prefs.edit().clear().apply()
        currentSession()
    }

    companion object {
        private const val PREFS_NAME = "memonest_session"
        private const val KEY_OWNER_ID = "owner_id"
        private const val KEY_DISPLAY_NAME = "display_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_IS_GUEST = "is_guest"
    }
}
