package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class TagDaoGetTagsForSongById : TagDaoTest() {
    @Test
    fun getTagsForSongById() {
        val tagIds = dao.insertAll(testTag1, testTag2, testTag3)
        val songDao = db.songDao()
        val songId = "testSongId"
        songDao.insert(SongEntity(songId, "", "", "", ""))
        val songTagCrossRefDao = db.songTagCrossRefDao()
        songTagCrossRefDao.insert(SongTagCrossRef(songId, tagIds[0]))
        songTagCrossRefDao.insert(SongTagCrossRef(songId, tagIds[1]))
        val retrievedTags = dao.getTagsForSongById(songId)
        assertEquals(2, retrievedTags.size)
        assertEquals(testTagName1, retrievedTags[0].name)
        assertEquals(testTagName2, retrievedTags[1].name)
        // TODO: Reescribir el assert para que no dependa del orden
    }
}