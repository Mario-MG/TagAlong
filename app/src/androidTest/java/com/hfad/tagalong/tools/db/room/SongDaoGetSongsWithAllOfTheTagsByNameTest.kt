package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong2
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong3
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag1
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag2
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag3
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class SongDaoGetSongsWithAllOfTheTagsByNameTest : SongDaoTest() {
    @Test
    fun getSongsWithAllOfTheTagsByName() {
        val songsToInsert = arrayOf(testSong1, testSong2, testSong3)
        dao.insertAll(*songsToInsert)
        val tagDao = db.tagDao()
        val tagsToInsert = arrayOf(testTag1, testTag2, testTag3)
        val tagIds = tagDao.insertAll(*tagsToInsert)
        val songTagCrossRefDao = db.songTagCrossRefDao()
        val songTagCrossRefsToInsert = arrayOf(
            SongTagCrossRef(testSong1.id, tagIds[0]),
            SongTagCrossRef(testSong1.id, tagIds[1]),
            SongTagCrossRef(testSong1.id, tagIds[2]),
            SongTagCrossRef(testSong2.id, tagIds[0]),
            SongTagCrossRef(testSong3.id, tagIds[1]),
            SongTagCrossRef(testSong3.id, tagIds[2])
        )
        songTagCrossRefDao.insertAll(*songTagCrossRefsToInsert)
        val retrievedSongs = dao.getSongsWithAllOfTheTagsByName(tagsToInsert[0].name, tagsToInsert[1].name)
        assertEquals(1, retrievedSongs.size)
        assertTrue(retrievedSongs.contains(testSong1))
        assertFalse(retrievedSongs.contains(testSong2))
        assertFalse(retrievedSongs.contains(testSong3))
    }
}