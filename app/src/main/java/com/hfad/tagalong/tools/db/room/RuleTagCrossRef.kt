package com.hfad.tagalong.tools.db.room

import androidx.room.*
import com.hfad.tagalong.types.PlaylistCreationRule

@Entity(
    primaryKeys = ["rule_id", "tag_id"],
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["id"],
        childColumns = ["rule_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = TagEntity::class,
        parentColumns = ["id"],
        childColumns = ["tag_id"],
        onDelete = ForeignKey.CASCADE
    )])
internal data class RuleTagCrossRef (
    @ColumnInfo(name = "rule_id") val ruleId: Long,
    @ColumnInfo(name = "tag_id") val tagId: Long
)

internal data class RuleWithTags (
    @Embedded val rule: RuleEntity,
    @Relation(
        parentColumn = "rule_id",
        entityColumn = "tag_id",
        associateBy = Junction(RuleTagCrossRef::class)
    )
    val tags: List<TagEntity>
) {
    companion object {
        fun toPlaylistCreationRule(ruleWithTags: RuleWithTags): PlaylistCreationRule {
            return PlaylistCreationRule(ruleWithTags.rule.id,
                ruleWithTags.tags.map(TagEntity::toTag), ruleWithTags.rule.playlistId,
                ruleWithTags.rule.optionality, ruleWithTags.rule.autoUpdate)
        }

        fun fromPlaylistCreationRule(playlistCreationRule: PlaylistCreationRule): RuleWithTags {
            return RuleWithTags(
                RuleEntity.fromPlaylistCreationRule(playlistCreationRule),
                playlistCreationRule.tags.map(TagEntity::fromTag)
            )
        }
    }
}