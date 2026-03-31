package com.memonest.app.data.repository

import com.memonest.app.data.model.JournalEntry

interface JournalRepository {
    suspend fun getEntries(ownerId: String): List<JournalEntry>
    suspend fun getEntry(entryId: Long, ownerId: String): JournalEntry?
    suspend fun upsert(entry: JournalEntry): Long
    suspend fun delete(entryId: Long, ownerId: String): Boolean
}
