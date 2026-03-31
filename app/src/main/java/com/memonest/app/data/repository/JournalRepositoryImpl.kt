package com.memonest.app.data.repository

import com.memonest.app.data.local.JournalEntryDao
import com.memonest.app.data.local.JournalEntryEntity
import com.memonest.app.data.model.JournalEntry

class JournalRepositoryImpl(
    private val dao: JournalEntryDao
) : JournalRepository {

    override suspend fun getEntries(ownerId: String): List<JournalEntry> {
        return dao.getEntriesForOwner(ownerId).map { it.toDomain() }
    }

    override suspend fun getEntry(entryId: Long, ownerId: String): JournalEntry? {
        return dao.getEntryById(entryId, ownerId)?.toDomain()
    }

    override suspend fun upsert(entry: JournalEntry): Long {
        return dao.upsert(entry.toEntity())
    }

    override suspend fun delete(entryId: Long, ownerId: String): Boolean {
        return dao.deleteById(entryId, ownerId) > 0
    }

    private fun JournalEntryEntity.toDomain(): JournalEntry = JournalEntry(
        id = id,
        ownerId = ownerId,
        title = title,
        content = content,
        tags = tags,
        photoUri = photoUri,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun JournalEntry.toEntity(): JournalEntryEntity = JournalEntryEntity(
        id = id,
        ownerId = ownerId,
        title = title,
        content = content,
        tags = tags,
        photoUri = photoUri,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
