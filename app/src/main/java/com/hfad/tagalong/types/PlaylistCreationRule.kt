package com.hfad.tagalong.types

import com.hfad.tagalong.config.Optionality

data class PlaylistCreationRule(
    var ruleId: Long,
    val tags: List<Tag>,
    val playlistId: String,
    val optionality: Optionality,
    val autoUpdate: Boolean
) {
    constructor(ruleId: Long, tags: List<Tag>, playlistId: String,
                optionality: Boolean, autoUpdate: Boolean) :
        this(ruleId, tags, playlistId, Optionality.forValue(optionality), autoUpdate)
}