package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class TagDaoDeleteByNameTest : TagDaoTest() {
    @Test
    fun delete() {
        dao.insertAll(testTag1, testTag2, testTag3)
        val deletedTagNum = dao.deleteByName(TagEntity.TagName(testTagName2))
        assertEquals(1, deletedTagNum)
        val retrievedTags = dao.getAll()
        assertEquals(2, retrievedTags.size)
        retrievedTags.forEach { tag -> assertNotEquals(testTagName2, tag.name) }
    }
}