package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule1
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule2
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule3
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class RuleDaoDeleteByIdTest : RuleDaoTest() {
    @Test
    fun deleteById() {
        val rulesToInsert = arrayOf(testRule1, testRule2, testRule3)
        val ruleIds = dao.insertAll(*rulesToInsert)
        val ruleToDeleteIndex = 1
        val deletedRulesNum = dao.deleteById(RuleEntity.Id(ruleIds[ruleToDeleteIndex]))
        assertEquals(deletedRulesNum, 1)
        val retrievedRules = dao.getAll()
        assertEquals(rulesToInsert.size - 1, retrievedRules.size)
        assertNull(retrievedRules.find { ruleWithTags ->
            ruleWithTags.rule.id == ruleIds[ruleToDeleteIndex]
        })
    }
}