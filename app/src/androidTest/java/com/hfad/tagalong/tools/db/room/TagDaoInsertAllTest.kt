package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class TagDaoInsertAllTest : TagDaoTest() {
    @Test
    fun insertAll() {
        val tagIds = dao.insertAll(testTag1, testTag2)
        assertEquals(2, tagIds.size)
        assertEquals(1, tagIds[0])
        assertEquals(2, tagIds[1])
        val retrievedTags = dao.getAll()
        assertEquals(2, retrievedTags.size)
        assertEquals(testTagName1, retrievedTags[0].name)
        assertEquals(testTagName2, retrievedTags[1].name)
        // TODO: Reescribir el assert para que no dependa del orden
    }
}