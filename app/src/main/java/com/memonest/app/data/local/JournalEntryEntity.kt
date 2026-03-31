package com.memonest.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val ownerId: String,
    val title: String,
    val content: String,
    val tags: String,
    val photoUri: String = "",
    val createdAt: Long,
    val updatedAt: Long
)
