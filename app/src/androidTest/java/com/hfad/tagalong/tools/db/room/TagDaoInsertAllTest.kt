package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.*
import org.junit.*
import org.junit.Assert.*

@HiltAndroidTest
@SmallTest
internal class TagDaoInsertAllTest : TagDaoTest() {
    @Test
    fun insertAll() {
        val tagName1 = "one"
        val tagName2 = "two"
        val testTag1 = TagEntity(tagName1)
        val testTag2 = TagEntity(tagName2)
        val tagIds = dao.insertAll(testTag1, testTag2)
        val retrievedTags = dao.getAll()
        assertEquals(2, retrievedTags.size)
        assertEquals(1, tagIds[0])
        assertEquals(tagName1, retrievedTags[0].name)
        assertEquals(2, tagIds[1])
        assertEquals(tagName2, retrievedTags[1].name)
    }
}