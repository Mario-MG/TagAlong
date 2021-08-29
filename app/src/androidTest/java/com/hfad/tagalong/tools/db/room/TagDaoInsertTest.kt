package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.*
import org.junit.*
import org.junit.Assert.*

@HiltAndroidTest
@SmallTest
internal class TagDaoInsertTest : TagDaoTest() {
    @Test
    fun insert() {
        val tagName = "one"
        val testTag = TagEntity(tagName)
        val tagId = dao.insert(testTag)
        val retrievedTags = dao.getAll()
        assertEquals(1, retrievedTags.size)
        assertEquals(1, tagId)
        assertEquals(tagName, retrievedTags[0].name)
    }
}