package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import com.hfad.tagalong.tools.db.room.utils.RuleTestUtil.testRule1
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class RuleDaoInsertTest : RuleDaoTest() {
    @Test
    fun insert() {
        val ruleId = dao.insert(testRule1)
        val retrievedRules = dao.getAll()
        assertEquals(1, retrievedRules.size)
        assertEquals(
            RuleWithTags(
                RuleEntity(ruleId, testRule1.playlistId, testRule1.optionality, testRule1.autoUpdate),
                emptyList()
            ),
            retrievedRules[0]
        )
    }
}