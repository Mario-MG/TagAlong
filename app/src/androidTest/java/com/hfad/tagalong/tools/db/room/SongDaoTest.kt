package com.hfad.tagalong.tools.db.room

internal abstract class SongDaoTest : DaoTest<SongDao>() {
    final override fun buildDao(): SongDao = db.songDao()
}