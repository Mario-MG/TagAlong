package com.hfad.tagalong.types

import com.hfad.tagalong.config.Optionality

class PlaylistCreationRule(
    var ruleId: Long?,
    val tags: ArrayList<String>,
    val playlistId: String,
    val optionality: Optionality,
    val autoUpdate: Boolean
)