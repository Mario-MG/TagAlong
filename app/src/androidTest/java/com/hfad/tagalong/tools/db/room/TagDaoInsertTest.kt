package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class TagDaoInsertTest : TagDaoTest() {
    @Test
    fun insert() {
        val tagId = dao.insert(testTag1)
        assertEquals(1, tagId)
        val retrievedTags = dao.getAll()
        assertEquals(1, retrievedTags.size)
        assertEquals(testTagName1, retrievedTags[0].name)
    }
}