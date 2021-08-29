package com.hfad.tagalong.tools.db.room

import androidx.room.*

@Dao
internal interface RuleTagCrossRefDao : BaseDao<RuleTagCrossRef> {
    @Query("SELECT * FROM RuleTagCrossRef")
    fun getAll(): List<RuleTagCrossRef>

    @Delete(entity = RuleTagCrossRef::class)
    fun deleteByRuleId(ruleId: RuleEntity.Id)
}