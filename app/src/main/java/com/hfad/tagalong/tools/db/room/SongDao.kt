package com.hfad.tagalong.tools.db.room

import androidx.room.*

@Dao
internal interface SongDao : BaseDao<SongEntity> {
    @Query("SELECT * FROM Song")
    fun getAll(): List<SongEntity>

    @Delete(entity = SongEntity::class)
    fun deleteById(vararg songIds: SongEntity.Id): Int

    @Query(
        """
        SELECT * FROM Song
        WHERE song_id IN (
            SELECT DISTINCT song_id FROM SongTagCrossRef ts
            JOIN Tag t ON ts.tag_id = t.tag_id
            WHERE t.name IN (:tagNames)
        )
    """
    )
    fun getSongsWithAnyOfTheTagsByName(vararg tagNames: String): List<SongEntity>

    @Query(
        """
        SELECT * FROM Song s
        JOIN SongTagCrossRef ts ON s.song_id = ts.song_id
        WHERE s.song_id NOT IN (
            SELECT ts.tag_id FROM SongTagCrossRef ts
            JOIN Tag t ON ts.tag_id = t.tag_id
            WHERE t.name NOT IN (:tagNames)
        )
    """
    )
    fun getSongsWithAllOfTheTagsByName(vararg tagNames: String): List<SongEntity>
}