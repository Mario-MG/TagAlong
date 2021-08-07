package com.hfad.tagalong.types

interface DbHelper {
    fun getAllTags(): List<Tag>

    fun insertTags(vararg tags: Tag)

    fun getTagsForSong(song: Track): List<Tag>

    fun getAllSongs(): List<Track>

    fun insertSongs(vararg songs: Track)

    fun deleteSongsById(vararg songIds: String)

    fun getSongsWithAnyOfTheTags(vararg tags: Tag): List<Track>

    fun getSongsWithAllOfTheTags(vararg tags: Tag): List<Track>

    fun insertSongWithTag(song: Track, tag: Tag)

    fun getAllRules(): List<PlaylistCreationRule>

    fun insertRule(rule: PlaylistCreationRule)

    fun deleteRuleById(ruleId: Long)
}