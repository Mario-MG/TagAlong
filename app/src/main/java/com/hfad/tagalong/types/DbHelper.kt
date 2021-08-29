package com.hfad.tagalong.types

interface DbHelper {
    fun getAllTags(): List<Tag>

    fun insertTag(tag: Tag): Long

    fun insertTags(vararg tags: Tag): List<Long>

    fun deleteTags(vararg tags: Tag): Int

    fun getTagsForSong(song: Track): List<Tag>

    fun getAllSongs(): List<Track>

    fun insertSong(song: Track): Long

    fun insertSongs(vararg songs: Track): List<Long>

    fun deleteSongsById(vararg songIds: String)

    fun getSongsWithAnyOfTheTags(vararg tags: Tag): List<Track>

    fun getSongsWithAllOfTheTags(vararg tags: Tag): List<Track>

    fun insertSongWithTag(song: Track, tag: Tag)

    fun deleteSongWithTag(song: Track, tag: Tag)

    fun getAllRules(): List<PlaylistCreationRule>

    fun getRulesFulfilledByTags(newTag: Tag, vararg originalTags: Tag): List<PlaylistCreationRule>

    fun insertRule(rule: PlaylistCreationRule): Long

    fun deleteRuleById(ruleId: Long)
}