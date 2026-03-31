package com.memonest.app.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateJournalEntryUseCaseTest {

    private val useCase = ValidateJournalEntryUseCase()

    @Test
    fun `isValid returns true when title and content are present`() {
        assertTrue(useCase.isValid("Today", "I completed my Kotlin journal app."))
    }

    @Test
    fun `isValid returns false when title is blank`() {
        assertFalse(useCase.isValid(" ", "Some content"))
    }

    @Test
    fun `isValid returns false when content is blank`() {
        assertFalse(useCase.isValid("Title", "  "))
    }
}
