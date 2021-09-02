package com.hfad.tagalong.tools.db.room

import androidx.room.*
import com.hfad.tagalong.types.PlaylistCreationRule

@Entity(tableName = "Rule")
internal data class RuleEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rule_id")
    val id: Long,

    @ColumnInfo(name = "playlist_id") val playlistId: String,
    val optionality: Boolean,
    @ColumnInfo(name = "auto_update") val autoUpdate: Boolean
) : DbEntity() {
    constructor(playlistId: String, optionality: Boolean, autoUpdate: Boolean) :
            this(0, playlistId, optionality, autoUpdate)

    companion object {
        fun fromPlaylistCreationRule(playlistCreationRule: PlaylistCreationRule): RuleEntity {
            return RuleEntity(playlistCreationRule.ruleId, playlistCreationRule.playlistId,
                    playlistCreationRule.optionality.value, playlistCreationRule.autoUpdate)
        }
    }

    internal data class Id (
        @ColumnInfo(name = "rule_id")
        val id: Long
    )
}