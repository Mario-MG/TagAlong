package com.hfad.tagalong.tools.db.room

internal abstract class TagDaoTest : DaoTest<TagDao>() {
    final override fun buildDao(): TagDao = db.tagDao()
}