package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag1
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag2
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class TagDaoInsertAllTest : TagDaoTest() {
    @Test
    fun insertAll() {
        val tagsToInsert = arrayOf(testTag1, testTag2)
        val tagIds = dao.insertAll(*tagsToInsert)
        assertEquals(tagsToInsert.size, tagIds.size)
        assertEquals(1, tagIds[0])
        assertEquals(2, tagIds[1])
        val retrievedTags = dao.getAll()
        assertEquals(tagsToInsert.size, retrievedTags.size)
        assertTrue(retrievedTags.contains(TagEntity(tagIds[0], tagsToInsert[0].name)))
        assertTrue(retrievedTags.contains(TagEntity(tagIds[1], tagsToInsert[1].name)))
    }
}