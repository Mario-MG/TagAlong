package com.hfad.tagalong.tools.db.room.utils

import com.hfad.tagalong.tools.db.room.TagEntity

internal object TagTestUtil {
    private const val testTagName1 = "tag_one"
    private const val testTagName2 = "tag_two"
    private const val testTagName3 = "tag_three"

    val testTag1: TagEntity
        get() = TagEntity(testTagName1)

    val testTag2: TagEntity
        get() = TagEntity(testTagName2)

    val testTag3: TagEntity
        get() = TagEntity(testTagName3)
}