package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class RuleDaoGetAllWithTagsTest : RuleDaoWithTagsTest() {
    @Test
    fun getAllWithTags() {
        val retrievedRules = dao.getAll()
        assertEquals(this.insertedRulesNum, retrievedRules.size)
        val ruleWithTags1 = retrievedRules.find { ruleWithTags ->
            ruleWithTags.rule == insertedRule1
        }
        assertNotNull(ruleWithTags1)
        assertTrue(ruleWithTags1!!.tags.contains(tags[0]))
        assertTrue(ruleWithTags1.tags.contains(tags[1]))
        val ruleWithTags2 = retrievedRules.find { ruleWithTags ->
            ruleWithTags.rule == insertedRule2
        }
        assertNotNull(ruleWithTags2)
        assertTrue(ruleWithTags2!!.tags.contains(tags[1]))
        assertTrue(ruleWithTags2.tags.contains(tags[2]))
    }
}