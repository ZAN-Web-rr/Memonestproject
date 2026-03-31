package com.memonest.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JournalEntryDao {
    @Query("SELECT * FROM journal_entries WHERE ownerId = :ownerId ORDER BY updatedAt DESC")
    suspend fun getEntriesForOwner(ownerId: String): List<JournalEntryEntity>

    @Query("SELECT * FROM journal_entries WHERE id = :entryId AND ownerId = :ownerId LIMIT 1")
    suspend fun getEntryById(entryId: Long, ownerId: String): JournalEntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: JournalEntryEntity): Long

    @Query("DELETE FROM journal_entries WHERE id = :entryId AND ownerId = :ownerId")
    suspend fun deleteById(entryId: Long, ownerId: String): Int
}
