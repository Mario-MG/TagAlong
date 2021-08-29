package com.hfad.tagalong.tools.db.room

internal abstract class TagDaoTest : DaoTest<TagDao>() {
    companion object {
        const val testTagName1 = "one"
        const val testTagName2 = "two"
        const val testTagName3 = "three"
    }

    val testTag1: TagEntity
        get() = TagEntity(testTagName1)

    val testTag2: TagEntity
        get() = TagEntity(testTagName2)

    val testTag3: TagEntity
        get() = TagEntity(testTagName3)

    final override fun buildDao(): TagDao = db.tagDao()
}