package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class SongTagCrossRefDaoInsertTest : SongTagCrossRefDaoTest() {
    @Test
    fun insert() {
        val songTagCrossRefToInsert = SongTagCrossRef(testSong1.id, this.tagIds[0])
        dao.insert(songTagCrossRefToInsert)
        val retrievedSongTagCrossRefs = dao.getAll()
        assertEquals(1, retrievedSongTagCrossRefs.size)
        assertEquals(songTagCrossRefToInsert, retrievedSongTagCrossRefs[0])
    }
}