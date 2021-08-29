package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.*
import org.junit.*
import org.junit.Assert.*

@HiltAndroidTest
@SmallTest
internal class TagDaoDeleteByNameTest : TagDaoTest() {
    @Test
    fun delete() {
        val tagName1 = "one"
        val tagName2 = "two"
        val tagName3 = "three"
        val testTag1 = TagEntity(tagName1)
        val testTag2 = TagEntity(tagName2)
        val testTag3 = TagEntity(tagName3)
        val tagIds = dao.insertAll(testTag1, testTag2, testTag3)
        val deletedTagNum = dao.deleteByName(TagEntity.TagName(tagName2))
        val retrievedTags = dao.getAll()
        assertEquals(1, deletedTagNum)
        assertEquals(2, retrievedTags.size)
        retrievedTags.forEach { tag -> assertNotEquals(tagName2, tag.name) }
    }
}