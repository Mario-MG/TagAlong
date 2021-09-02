package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule1
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule2
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class RuleDaoInsertAllTest : RuleDaoTest() {
    @Test
    fun insertAll() {
        val rulesToInsert = arrayOf(testRule1, testRule2)
        val ruleIds = dao.insertAll(*rulesToInsert)
        assertEquals(rulesToInsert.size, ruleIds.size)
        val retrievedRules = dao.getAll()
        assertEquals(rulesToInsert.size, retrievedRules.size)
        assertTrue(retrievedRules.contains(RuleWithTags(
            RuleEntity(ruleIds[0], rulesToInsert[0].playlistId, rulesToInsert[0].optionality,
                rulesToInsert[0].autoUpdate),
            emptyList()
        )))
        assertTrue(retrievedRules.contains(RuleWithTags(
            RuleEntity(ruleIds[1], rulesToInsert[1].playlistId, rulesToInsert[1].optionality,
                rulesToInsert[1].autoUpdate),
            emptyList()
        )))
    }
}