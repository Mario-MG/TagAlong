package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
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
        assertEquals(testSong1, retrievedSongs[0])
    }
}