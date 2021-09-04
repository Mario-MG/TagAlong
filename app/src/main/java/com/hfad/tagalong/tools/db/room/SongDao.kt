package com.hfad.tagalong.tools.db.room

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
internal interface SongDao : BaseDao<SongEntity> {
    @Query("SELECT * FROM Song")
    fun getAll(): List<SongEntity>

    @Delete(entity = SongEntity::class)
    fun deleteById(vararg songIds: SongEntity.Id): Int

    @Query("""
        SELECT DISTINCT s.* FROM Song s
        JOIN SongTagCrossRef st ON s.song_id = st.song_id
        JOIN Tag t ON st.tag_id = t.tag_id
        WHERE t.name IN (:tagNames)
    """)
    fun getSongsWithAnyOfTheTagsByName(vararg tagNames: String): List<SongEntity>

    @Query("""
        SELECT s.* FROM Song s
        JOIN SongTagCrossRef st ON s.song_id = st.song_id
        JOIN Tag t ON st.tag_id = t.tag_id
        WHERE t.name IN (:tagNames)
        GROUP BY s.song_id
        HAVING COUNT(*) = JSON_ARRAY_LENGTH(JSON_ARRAY(:tagNames))
    """)
    fun getSongsWithAllOfTheTagsByName(vararg tagNames: String): List<SongEntity>
}