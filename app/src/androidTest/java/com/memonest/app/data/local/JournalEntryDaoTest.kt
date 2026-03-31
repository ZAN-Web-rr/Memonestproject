package com.memonest.app.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JournalEntryDaoTest {

    private lateinit var database: MemoNestDatabase
    private lateinit var dao: JournalEntryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MemoNestDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.journalEntryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndReadByOwner() = runBlocking {
        dao.upsert(
            JournalEntryEntity(
                ownerId = "owner-a",
                title = "Title",
                content = "Body",
                tags = "tag",
                photoUri = "content://entry/photo",
                createdAt = 1L,
                updatedAt = 2L
            )
        )

        val entries = dao.getEntriesForOwner("owner-a")
        assertEquals(1, entries.size)
        assertEquals("Title", entries.first().title)
        assertEquals("content://entry/photo", entries.first().photoUri)

        val fetched = dao.getEntryById(entries.first().id, "owner-a")
        assertNotNull(fetched)
    }
}
