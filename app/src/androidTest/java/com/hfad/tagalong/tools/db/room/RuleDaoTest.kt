package com.hfad.tagalong.tools.db.room

internal abstract class RuleDaoTest : DaoTest<RuleDao>() {
    final override fun buildDao(): RuleDao = db.ruleDao()
}