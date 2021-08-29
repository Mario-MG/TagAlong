package com.hfad.tagalong.tools.db.room

import androidx.room.*

@Dao
internal interface SongTagCrossRefDao : BaseDao<SongTagCrossRef> {
    // TODO: Cómo averiguar si ha habido conflicto al insertar (ver si puede funcionar ABORT + @Transaction)

    @Query("SELECT * FROM SongTagCrossRef")
    fun getAll(): List<SongTagCrossRef>

    @Delete
    fun delete(songTagCrossRef: SongTagCrossRef)

//    @Transaction
//    @Query("SELECT * FROM Tag WHERE name = :tagName")
//    fun getSongsTaggedWith(tagName: String): TagWithSongs
//
//    @Transaction
//    @Query("SELECT * FROM Song WHERE id = :songId")
//    fun getTagsForSongById(songId: String): SongWithTags
}