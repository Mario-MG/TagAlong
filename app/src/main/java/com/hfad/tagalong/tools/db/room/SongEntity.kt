package com.hfad.tagalong.tools.db.room

import androidx.room.*
import com.hfad.tagalong.types.Track

@Entity(tableName = "Song")
internal data class SongEntity (
    @PrimaryKey val id: String,

    val name: String,
    val album: String,
    val artists: String,
    @ColumnInfo(name = "image_url") val imageUrl: String
) {
    companion object {
        fun toTrack(songEntity: SongEntity): Track {
            return Track(songEntity.id, songEntity.name, songEntity.album,
                songEntity.artists.split(", "),
                if (songEntity.imageUrl != "") songEntity.imageUrl else null)
        }

        fun fromTrack(track: Track): SongEntity {
            return SongEntity(track.id, track.name, track.album,
                track.artists.joinToString(", "), track.imageUrl ?: "")
        }
    }
}

internal data class SongId (
    val id: String
)