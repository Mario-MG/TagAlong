package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong2
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class SongTagCrossRefDaoDeleteTest : SongTagCrossRefDaoTest() {
    @Test
    fun delete() {
        val songTagCrossRefsToInsert = arrayOf(
            SongTagCrossRef(testSong1.id, this.tagIds[0]),
            SongTagCrossRef(testSong1.id, this.tagIds[1]),
            SongTagCrossRef(testSong2.id, this.tagIds[0])
        )
        dao.insertAll(*songTagCrossRefsToInsert)
        val songTagCrossRefToDeleteIndex = 1
        val songTagCrossRefToDelete = songTagCrossRefsToInsert[songTagCrossRefToDeleteIndex]
        dao.delete(songTagCrossRefToDelete)
        val retrievedSongTagCrossRefs = dao.getAll()
        assertEquals(songTagCrossRefsToInsert.size - 1, retrievedSongTagCrossRefs.size)
        assertFalse(retrievedSongTagCrossRefs.contains(songTagCrossRefToDelete))
    }
}