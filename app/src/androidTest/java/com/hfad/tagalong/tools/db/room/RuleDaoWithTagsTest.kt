package com.hfad.tagalong.tools.db.room

import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule1
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule2
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule3
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag1
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag2
import com.hfad.tagalong.tools.db.room.utils.TagTestUtil.testTag3
import org.junit.Before

internal abstract class RuleDaoWithTagsTest : RuleDaoTest() {
    protected var insertedRulesNum = 0
    protected lateinit var insertedRule1: RuleEntity
    protected lateinit var insertedRule2: RuleEntity
    protected lateinit var insertedRule3: RuleEntity
    protected lateinit var tags: List<TagEntity>

    @Before
    override fun setup() {
        super.setup()
        populateTables()
    }

    private fun populateTables() {
        val tagDao = db.tagDao()
        val tagsToInsert = arrayOf(testTag1, testTag2, testTag3)
        tagDao.insertAll(*tagsToInsert)
        val rulesToInsert = arrayOf(testRule1, testRule2, testRule3)
        insertedRulesNum = rulesToInsert.size
        val ruleIds = dao.insertAll(*rulesToInsert)
        insertedRule1 = RuleEntity(ruleIds[0], rulesToInsert[0].playlistId,
            rulesToInsert[0].optionality, rulesToInsert[0].autoUpdate)
        insertedRule2 = RuleEntity(ruleIds[1], rulesToInsert[1].playlistId,
            rulesToInsert[1].optionality, rulesToInsert[1].autoUpdate)
        insertedRule3 = RuleEntity(ruleIds[2], rulesToInsert[2].playlistId,
            rulesToInsert[2].optionality, rulesToInsert[2].autoUpdate)
        tags = tagDao.getAll()
        val ruleTagCrossRefDao = db.ruleTagCrossRefDao()
        ruleTagCrossRefDao.insertAll(
            RuleTagCrossRef(ruleIds[0], tags[0].id),
            RuleTagCrossRef(ruleIds[0], tags[1].id),
            RuleTagCrossRef(ruleIds[1], tags[1].id),
            RuleTagCrossRef(ruleIds[1], tags[2].id)
        )
    }
}