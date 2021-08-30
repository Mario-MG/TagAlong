package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong2
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong3
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class SongDaoDeleteByIdTest : SongDaoTest() {
    @Test
    fun deleteById() {
        val songsToInsert = arrayOf(testSong1, testSong2, testSong3)
        dao.insertAll(*songsToInsert)
        val songToDeleteIndex = 1
        val songToDelete = songsToInsert[songToDeleteIndex]
        val deletedSongsNum = dao.deleteById(SongEntity.Id(songToDelete.id))
        assertEquals(1, deletedSongsNum)
        val retrievedSongs = dao.getAll()
        assertEquals(songsToInsert.size - 1, retrievedSongs.size)
        assertFalse(retrievedSongs.contains(songToDelete))
    }
}