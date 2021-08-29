package com.hfad.tagalong.tools.db.room

import androidx.room.*

@Dao
internal interface TagDao : BaseDao<TagEntity> {
    @Query("SELECT * FROM Tag")
    fun getAll(): List<TagEntity>

    @Query(
        """
        SELECT * FROM Tag
        WHERE tag_id IN (
            SELECT tag_id FROM SongTagCrossRef
            WHERE song_id = :songId
        )
    """
    )
    fun getTagsForSongById(songId: String): List<TagEntity>

    @Delete(entity = TagEntity::class)
    fun deleteByName(vararg tagName: TagEntity.TagName): Int
}