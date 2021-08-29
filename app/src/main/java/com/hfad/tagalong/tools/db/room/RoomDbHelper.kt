package com.hfad.tagalong.tools.db.room

import android.content.Context
import com.hfad.tagalong.types.*

class RoomDbHelper(context: Context) : DbHelper {
    private val db = RoomDb.getInstance(context) // TODO: Inyectar??

    private val tagDao = db.tagDao()
    private val songDao = db.songDao()
    private val songTagCrossRefDao = db.songTagCrossRefDao()
    private val ruleDao = db.ruleDao()
    private val ruleTagCrossRefDao = db.ruleTagCrossRefDao()

    override fun getAllTags(): List<Tag> {
        return tagDao.getAll().map(TagEntity::toTag)
    }

    override fun insertTag(tag: Tag): Long {
        return tagDao.insert(TagEntity.fromTag(tag))
    }

    override fun insertTags(vararg tags: Tag): List<Long> {
        return tagDao.insertAll(*tags.map(TagEntity::fromTag).toTypedArray())
    }

    override fun deleteTags(vararg tags: Tag): Int {
        return tagDao.deleteByName(*tags.map(TagEntity.TagName::fromTag).toTypedArray())
    }

    override fun getTagsForSong(song: Track): List<Tag> {
        return tagDao.getTagsForSongById(song.id).map(TagEntity::toTag)
    }

    override fun getAllSongs(): List<Track> {
        return songDao.getAll().map(SongEntity::toTrack)
    }

    override fun insertSong(song: Track): Long {
        return songDao.insert(SongEntity.fromTrack(song))
    }

    override fun insertSongs(vararg songs: Track): List<Long> {
        return songDao.insertAll(*songs.map(SongEntity::fromTrack).toTypedArray())
    }

    override fun deleteSongsById(vararg songIds: String) {
        songDao.deleteById(*songIds.map(SongEntity::Id).toTypedArray())
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

    override fun deleteSongWithTag(song: Track, tag: Tag) {
        songTagCrossRefDao.delete(SongTagCrossRef(song.id, tag.id))
    }

    override fun getAllRules(): List<PlaylistCreationRule> {
        return ruleDao.getAll().map(RuleWithTags::toPlaylistCreationRule)
    }

    override fun getRulesFulfilledByTags(newTag: Tag, vararg originalTags: Tag): List<PlaylistCreationRule> {
        return ruleDao
            .getRulesFulfilledByTagNames(newTag.name, *originalTags.map(Tag::name).toTypedArray())
            .map(RuleWithTags::toPlaylistCreationRule)
    }

    override fun insertRule(rule: PlaylistCreationRule): Long {
        val ruleId = ruleDao.insert(RuleEntity.fromPlaylistCreationRule(rule))
        rule.tags.forEach { ruleTagCrossRefDao.insert(RuleTagCrossRef(rule.ruleId, it.id)) }
        return ruleId
    }

    override fun deleteRuleById(ruleId: Long) {
        ruleDao.deleteById(RuleEntity.Id(ruleId))
    }
}