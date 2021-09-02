package com.hfad.tagalong.tools.db.room

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test

@HiltAndroidTest
@SmallTest
internal class RuleDaoGetByIdWithTagsTest : RuleDaoWithTagsTest() {
    @Test
    fun getByIdWithTags() {
        val retrievedRuleWithTags = dao.getById(this.insertedRule1.id)
        assertNotNull(retrievedRuleWithTags)
        assertEquals(retrievedRuleWithTags!!.rule, this.insertedRule1)
        assertTrue(retrievedRuleWithTags.tags.contains(this.tags[0]))
        assertTrue(retrievedRuleWithTags.tags.contains(this.tags[1]))
    }
}