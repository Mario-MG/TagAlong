package com.hfad.tagalong.tools.db.room.utils

import com.hfad.tagalong.tools.db.room.TagEntity

internal object TagTestUtil {
    private const val testTagName1 = "one"
    private const val testTagName2 = "two"
    private const val testTagName3 = "three"

    val testTag1: TagEntity
        get() = TagEntity(testTagName1)

    val testTag2: TagEntity
        get() = TagEntity(testTagName2)

    val testTag3: TagEntity
        get() = TagEntity(testTagName3)
}