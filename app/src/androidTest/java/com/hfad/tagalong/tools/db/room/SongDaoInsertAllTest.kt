package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong2
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class SongDaoInsertAllTest : SongDaoTest() {
    @Test
    fun insertAll() {
        val songsToInsert = arrayOf(testSong1, testSong2)
        val insertAllResult = dao.insertAll(*songsToInsert)
        assertEquals(songsToInsert.size, insertAllResult.size)
        val retrievedSongs = dao.getAll()
        assertEquals(songsToInsert.size, retrievedSongs.size)
        assertTrue(retrievedSongs.contains(songsToInsert[0]))
        assertTrue(retrievedSongs.contains(songsToInsert[1]))
    }
}