package com.hfad.tagalong.tools.db.room

import androidx.room.*
import com.hfad.tagalong.types.PlaylistCreationRule

@Entity(tableName = "Rule")
internal data class RuleEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,

    @ColumnInfo(name = "playlist_id") val playlistId: String,
    val optionality: Boolean,
    @ColumnInfo(name = "auto_update") val autoUpdate: Boolean
) {
    companion object {
        fun fromPlaylistCreationRule(playlistCreationRule: PlaylistCreationRule): RuleEntity {
            return RuleEntity(playlistCreationRule.ruleId, playlistCreationRule.playlistId,
                    playlistCreationRule.optionality.value, playlistCreationRule.autoUpdate)
        }
    }
}

internal data class RuleId (
    val id: Long
)