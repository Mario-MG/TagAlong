package com.hfad.tagalong.tools.db.room

import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong1
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong2
import com.hfad.tagalong.tools.db.room.utils.SongTestUtil.testSong3
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag1
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag2
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag3
import org.junit.Before

internal abstract class SongTagCrossRefDaoTest : DaoTest<SongTagCrossRefDao>() {
    lateinit var tagIds: List<Long>

    final override fun buildDao(): SongTagCrossRefDao = db.songTagCrossRefDao()

    @Before
    override fun setup() {
        super.setup()
        populateSongAndTagTables()
    }

    private fun populateSongAndTagTables() {
        val songDao = db.songDao()
        val songsToInsert = arrayOf(testSong1, testSong2, testSong3)
        songDao.insertAll(*songsToInsert)
        val tagDao = db.tagDao()
        val tagsToInsert = arrayOf(testTag1, testTag2, testTag3)
        tagIds = tagDao.insertAll(*tagsToInsert)
    }
}