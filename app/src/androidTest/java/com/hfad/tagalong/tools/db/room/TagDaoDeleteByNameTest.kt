package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag1
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag2
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag3
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class TagDaoDeleteByNameTest : TagDaoTest() {
    @Test
    fun delete() {
        val tagsToInsert = arrayOf(testTag1, testTag2, testTag3)
        val tagIds = dao.insertAll(*tagsToInsert)
        val tagToDeleteIndex = 1
        val tagToDelete = tagsToInsert[tagToDeleteIndex]
        val deletedTagsNum = dao.deleteByName(TagEntity.TagName(tagToDelete.name))
        assertEquals(1, deletedTagsNum)
        val retrievedTags = dao.getAll()
        assertEquals(tagsToInsert.size - 1, retrievedTags.size)
        assertFalse(retrievedTags.contains(TagEntity(tagIds[tagToDeleteIndex], tagToDelete.name)))
    }
}