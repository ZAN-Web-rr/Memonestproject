package com.memonest.app.domain

import com.memonest.app.data.model.JournalEntry
import com.memonest.app.data.repository.JournalRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveJournalEntryUseCaseTest {

    @Test
    fun `save succeeds with valid data`() = runTest {
        val repository = FakeJournalRepository()
        val useCase = SaveJournalEntryUseCase(repository, ValidateJournalEntryUseCase())

        val result = useCase(
            JournalEntry(
                ownerId = "owner-1",
                title = "  Good day  ",
                content = "  I learned Room today.  ",
                tags = "study,android",
                createdAt = 0L,
                updatedAt = 0L
            )
        )

        assertTrue(result.isSuccess)
        assertTrue(repository.entries.size == 1)
        assertTrue(repository.entries.first().title == "Good day")
        assertTrue(repository.entries.first().photoUri.isEmpty())
    }

    @Test
    fun `save fails with invalid data`() = runTest {
        val repository = FakeJournalRepository()
        val useCase = SaveJournalEntryUseCase(repository, ValidateJournalEntryUseCase())

        val result = useCase(
            JournalEntry(
                ownerId = "owner-1",
                title = " ",
                content = " ",
                tags = "",
                createdAt = 0L,
                updatedAt = 0L
            )
        )

        assertTrue(result.isFailure)
        assertTrue(repository.entries.isEmpty())
    }

    private class FakeJournalRepository : JournalRepository {
        val entries = mutableListOf<JournalEntry>()

        override suspend fun getEntries(ownerId: String): List<JournalEntry> {
            return entries.filter { it.ownerId == ownerId }
        }

        override suspend fun getEntry(entryId: Long, ownerId: String): JournalEntry? {
            return entries.find { it.id == entryId && it.ownerId == ownerId }
        }

        override suspend fun upsert(entry: JournalEntry): Long {
            if (entry.id == 0L) {
                val newId = (entries.maxOfOrNull { it.id } ?: 0L) + 1L
                entries.add(entry.copy(id = newId))
                return newId
            }
            entries.removeAll { it.id == entry.id && it.ownerId == entry.ownerId }
            entries.add(entry)
            return entry.id
        }

        override suspend fun delete(entryId: Long, ownerId: String): Boolean {
            return entries.removeIf { it.id == entryId && it.ownerId == ownerId }
        }
    }
}
