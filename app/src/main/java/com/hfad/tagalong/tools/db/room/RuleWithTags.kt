package com.hfad.tagalong.tools.db.room

import androidx.room.*
import com.hfad.tagalong.types.PlaylistCreationRule

internal data class RuleWithTags (
    @Embedded val rule: RuleEntity,
    @Relation(
        parentColumn = "rule_id",
        entityColumn = "tag_id",
        associateBy = Junction(
            RuleTagCrossRef::class,
            parentColumn = "rule_id",
            entityColumn = "tag_id"
        )
    )
    val tags: List<TagEntity>
) {
    companion object {
        fun toPlaylistCreationRule(ruleWithTags: RuleWithTags): PlaylistCreationRule {
            return PlaylistCreationRule(
                ruleWithTags.rule.id,
                ruleWithTags.tags.map(TagEntity.Companion::toTag), ruleWithTags.rule.playlistId,
                ruleWithTags.rule.optionality, ruleWithTags.rule.autoUpdate
            )
        }

        fun fromPlaylistCreationRule(playlistCreationRule: PlaylistCreationRule): RuleWithTags {
            return RuleWithTags(
                RuleEntity.fromPlaylistCreationRule(playlistCreationRule),
                playlistCreationRule.tags.map(TagEntity.Companion::fromTag)
            )
        }
    }
}