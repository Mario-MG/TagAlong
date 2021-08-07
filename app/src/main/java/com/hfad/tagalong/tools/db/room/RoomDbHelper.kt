package com.hfad.tagalong.tools.db.room

import android.content.Context
import com.hfad.tagalong.types.*

class RoomDbHelper(context: Context) : DbHelper {
    private val db = RoomDb.getInstance(context)

    private val tagDao = db.tagDao()
    private val songDao = db.songDao()
    private val songTagCrossRefDao = db.songTagCrossRefDao()
    private val ruleDao = db.ruleDao()
    private val ruleTagCrossRefDao = db.ruleTagCrossRefDao()

    override fun getAllTags(): List<Tag> {
        return tagDao.getAll().map(TagEntity::toTag)
    }

    override fun insertTags(vararg tags: Tag) {
        tagDao.insert(*tags.map(TagEntity::fromTag).toTypedArray())
    }

    override fun getTagsForSong(song: Track): List<Tag> {
        return tagDao.getTagsForSongById(song.id).map(TagEntity::toTag)
    }

    override fun getAllSongs(): List<Track> {
        return songDao.getAll().map(SongEntity::toTrack)
    }

    override fun insertSongs(vararg songs: Track) {
        songDao.insert(*songs.map(SongEntity::fromTrack).toTypedArray())
    }

    override fun deleteSongsById(vararg songIds: String) {
        songDao.deleteById(*songIds.map(::SongId).toTypedArray())
    }

    override fun getSongsWithAnyOfTheTags(vararg tags: Tag): List<Track> {
        return songDao.getSongsWithAnyOfTheTagsByName(*tags.map(Tag::name).toTypedArray())
            .map(SongEntity::toTrack)
    }

    override fun getSongsWithAllOfTheTags(vararg tags: Tag): List<Track> {
        return songDao.getSongsWithAllOfTheTagsByName(*tags.map(Tag::name).toTypedArray())
            .map(SongEntity::toTrack)
    }

    override fun insertSongWithTag(song: Track, tag: Tag) {
        songDao.insert(SongEntity.fromTrack(song))
        tagDao.insert(TagEntity.fromTag(tag))
        songTagCrossRefDao.insert(SongTagCrossRef(song.id, tag.id))
    }

    override fun getAllRules(): List<PlaylistCreationRule> {
        return ruleDao.getAll().map(RuleWithTags::toPlaylistCreationRule)
    }

    override fun insertRule(rule: PlaylistCreationRule) {
        ruleDao.insert(RuleEntity.fromPlaylistCreationRule(rule))
        rule.tags.forEach { ruleTagCrossRefDao.insert(RuleTagCrossRef(rule.ruleId, it.id)) }
    }

    override fun deleteRuleById(ruleId: Long) {
        ruleDao.deleteById(RuleId(ruleId))
    }
}