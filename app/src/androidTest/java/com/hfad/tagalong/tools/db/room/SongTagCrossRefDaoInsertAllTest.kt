package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong2
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class SongTagCrossRefDaoInsertAllTest : SongTagCrossRefDaoTest() {
    @Test
    fun insertAll() {
        val songTagCrossRefsToInsert = arrayOf(
            SongTagCrossRef(testSong1.id, this.tagIds[0]),
            SongTagCrossRef(testSong2.id, this.tagIds[1])
        )
        dao.insertAll(*songTagCrossRefsToInsert)
        val retrievedSongTagCrossRefs = dao.getAll()
        assertEquals(songTagCrossRefsToInsert.size, retrievedSongTagCrossRefs.size)
        assertTrue(retrievedSongTagCrossRefs.contains(songTagCrossRefsToInsert[0]))
        assertTrue(retrievedSongTagCrossRefs.contains(songTagCrossRefsToInsert[1]))
    }
}