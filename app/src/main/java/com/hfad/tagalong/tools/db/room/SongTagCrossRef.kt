package com.hfad.tagalong.tools.db.room

import androidx.room.*

@Entity(
    primaryKeys = ["song_id", "tag_id"],
    foreignKeys = [ForeignKey(
        entity = SongEntity::class,
        parentColumns = ["song_id"],
        childColumns = ["song_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = TagEntity::class,
        parentColumns = ["tag_id"],
        childColumns = ["tag_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
internal data class SongTagCrossRef (
    @ColumnInfo(name = "song_id") val songId: String,
    @ColumnInfo(name = "tag_id") val tagId: Long
)

//internal data class TagWithSongs (
//    @Embedded val tag: TagEntity,
//    @Relation(
//        parentColumn = "tag_id",
//        entityColumn = "song_id",
//        associateBy = Junction(SongTagCrossRef::class)
//    )
//    val songs: List<SongEntity>
//)
//
//internal data class SongWithTags (
//    @Embedded val song: SongEntity,
//    @Relation(
//        parentColumn = "song_id",
//        entityColumn = "tag_id",
//        associateBy = Junction(SongTagCrossRef::class)
//    )
//    val tags: List<TagEntity>
//)