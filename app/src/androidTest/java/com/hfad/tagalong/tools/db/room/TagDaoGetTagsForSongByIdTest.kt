package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong2
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag1
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag2
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag3
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class TagDaoGetTagsForSongByIdTest : TagDaoTest() {
    @Test
    fun getTagsForSongById() {
        val tagsToInsert = arrayOf(testTag1, testTag2, testTag3)
        val tagIds = dao.insertAll(*tagsToInsert)
        val songDao = db.songDao()
        val songsToInsert = arrayOf(testSong1, testSong2)
        songDao.insertAll(*songsToInsert)
        val songTagCrossRefDao = db.songTagCrossRefDao()
        val songTagCrossRefsToInsert = arrayOf(
            SongTagCrossRef(testSong1.id, tagIds[0]),
            SongTagCrossRef(testSong1.id, tagIds[1]),
            SongTagCrossRef(testSong2.id, tagIds[2])
        )
        songTagCrossRefDao.insertAll(*songTagCrossRefsToInsert)
        val retrievedTags = dao.getTagsForSongById(testSong1.id)
        assertEquals(2, retrievedTags.size)
        assertTrue(retrievedTags.contains(TagEntity(tagIds[0], tagsToInsert[0].name)))
        assertTrue(retrievedTags.contains(TagEntity(tagIds[1], tagsToInsert[1].name)))
        assertFalse(retrievedTags.contains(TagEntity(tagIds[2], tagsToInsert[2].name)))
    }
}