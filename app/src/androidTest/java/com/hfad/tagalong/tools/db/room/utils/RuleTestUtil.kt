package com.hfad.tagalong.tools.db.room.utils

import com.hfad.tagalong.tools.db.room.RuleEntity

internal object RuleTestUtil {
    private const val testplaylistId1 = "playlist_1"
    private const val testplaylistId2 = "playlist_2"
    private const val testplaylistId3 = "playlist_3"

    val testRule1: RuleEntity
        get() = RuleEntity(testplaylistId1, optionality = true, autoUpdate = true)

    val testRule2: RuleEntity
        get() = RuleEntity(testplaylistId2, optionality = false, autoUpdate = false)

    val testRule3: RuleEntity
        get() = RuleEntity(testplaylistId3, optionality = false, autoUpdate = true)

}