package com.hfad.tagalong.tools.db.room

import androidx.room.*
import com.hfad.tagalong.types.Tag

@Entity(tableName = "Tag", indices = [Index(value = ["name"], unique = true)])
internal data class TagEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,

    val name: String
) {
    companion object {
        fun toTag(tagEntity: TagEntity): Tag {
            return Tag(tagEntity.id, tagEntity.name)
        }

        fun fromTag(tag: Tag): TagEntity {
            return TagEntity(tag.id, tag.name)
        }
    }
}