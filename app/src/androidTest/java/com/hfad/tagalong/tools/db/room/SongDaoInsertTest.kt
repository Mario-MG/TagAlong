package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class SongDaoInsertTest : SongDaoTest() {
    @Test
    fun insert() {
        dao.insert(testSong1)
        val retrievedSongs = dao.getAll()
        assertEquals(1, retrievedSongs.size)
        assertEquals(testSongId1, retrievedSongs[0].id)
    }
}