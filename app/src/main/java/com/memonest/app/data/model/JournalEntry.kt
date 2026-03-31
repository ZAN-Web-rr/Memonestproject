package com.memonest.app.data.model

data class JournalEntry(
    val id: Long = 0L,
    val ownerId: String,
    val title: String,
    val content: String,
    val tags: String,
    val photoUri: String = "",
    val createdAt: Long,
    val updatedAt: Long
)
