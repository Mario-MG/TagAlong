package com.hfad.tagalong.tools.db.room

import androidx.room.*

@Dao
internal interface RuleDao : BaseDao<RuleEntity> {
    @Transaction
    @Query("SELECT * FROM Rule")
    fun getAll(): List<RuleWithTags>

    @Transaction
    @Query("SELECT * FROM Rule WHERE id = :ruleId")
    fun getById(ruleId: Long): RuleWithTags

    @Delete(entity = RuleEntity::class)
    fun deleteById(vararg ruleIds: RuleId)

    @Query(
        """
        SELECT * FROM Rule r
        JOIN RuleTagCrossRef rt ON r.id = rt.rule_id
        JOIN Tag t ON rt.tag_id = t.id
        WHERE r.auto_update = 1
        AND ((
                r.optionality = 1
                AND t.name = :newTag
                AND t.name NOT IN (:originalTags) -- TODO: Comprobar que esto funciona
            ) OR (
                r.optionality = 0
                AND t.name = :newTag -- TODO: Comprobar que esto funciona (en caso afirmativo, refactorizar fuera del OR)
                AND rt.rule_id NOT IN (
                    SELECT DISTINCT rt.rule_id FROM RuleTagCrossRef rt
                    JOIN Tag t on rt.tag_id = t.id
                    WHERE t.name NOT IN (:newTag, :originalTags)
                )
            )
        )
    """
    )
    fun getRulesFulfilledByTagNames(newTag: String, vararg originalTags: String): List<RuleEntity>
}