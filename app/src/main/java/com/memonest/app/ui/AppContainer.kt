package com.memonest.app.ui

import android.content.Context
import com.memonest.app.auth.UserSessionManager
import com.memonest.app.data.local.MemoNestDatabase
import com.memonest.app.data.repository.JournalRepository
import com.memonest.app.data.repository.JournalRepositoryImpl
import com.memonest.app.domain.SaveJournalEntryUseCase
import com.memonest.app.domain.ValidateJournalEntryUseCase

object AppContainer {
    private var repository: JournalRepository? = null
    private var sessionManager: UserSessionManager? = null
    private var themePreferenceManager: ThemePreferenceManager? = null

    fun repository(context: Context): JournalRepository {
        return repository ?: synchronized(this) {
            repository ?: JournalRepositoryImpl(
                MemoNestDatabase.getInstance(context).journalEntryDao()
            ).also { repository = it }
        }
    }

    fun sessionManager(context: Context): UserSessionManager {
        return sessionManager ?: synchronized(this) {
            sessionManager ?: UserSessionManager(context).also { sessionManager = it }
        }
    }

    fun saveEntryUseCase(context: Context): SaveJournalEntryUseCase {
        return SaveJournalEntryUseCase(repository(context), ValidateJournalEntryUseCase())
    }

    fun themePreferenceManager(context: Context): ThemePreferenceManager {
        return themePreferenceManager ?: synchronized(this) {
            themePreferenceManager ?: ThemePreferenceManager(context).also { themePreferenceManager = it }
        }
    }
}
