package com.hfad.tagalong.tools.db.room

import androidx.room.*

@Entity(
    primaryKeys = ["rule_id", "tag_id"],
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rule_id"],
        childColumns = ["rule_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = TagEntity::class,
        parentColumns = ["tag_id"],
        childColumns = ["tag_id"],
        onDelete = ForeignKey.CASCADE
    )])
internal data class RuleTagCrossRef (
    @ColumnInfo(name = "rule_id") val ruleId: Long,
    @ColumnInfo(name = "tag_id") val tagId: Long
) : DbEntity()