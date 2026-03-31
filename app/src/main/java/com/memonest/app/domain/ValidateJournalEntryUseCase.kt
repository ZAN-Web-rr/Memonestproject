package com.memonest.app.domain

import com.memonest.app.data.model.JournalEntry

class ValidateJournalEntryUseCase {
    fun isValid(title: String, content: String): Boolean {
        return title.trim().isNotEmpty() && content.trim().isNotEmpty()
    }

    fun sanitize(entry: JournalEntry): JournalEntry {
        return entry.copy(
            title = entry.title.trim(),
            content = entry.content.trim(),
            tags = entry.tags.trim()
        )
    }
}
