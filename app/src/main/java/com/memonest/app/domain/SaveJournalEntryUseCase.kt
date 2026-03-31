package com.memonest.app.domain

import com.memonest.app.data.model.JournalEntry
import com.memonest.app.data.repository.JournalRepository

class SaveJournalEntryUseCase(
    private val repository: JournalRepository,
    private val validator: ValidateJournalEntryUseCase
) {
    suspend operator fun invoke(entry: JournalEntry): Result<Long> {
        if (!validator.isValid(entry.title, entry.content)) {
            return Result.failure(IllegalArgumentException("Invalid entry"))
        }
        val now = System.currentTimeMillis()
        val sanitized = validator.sanitize(entry)
        val prepared = sanitized.copy(
            createdAt = if (sanitized.createdAt == 0L) now else sanitized.createdAt,
            updatedAt = now
        )
        val id = repository.upsert(prepared)
        return Result.success(id)
    }
}
