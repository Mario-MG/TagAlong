package com.hfad.tagalong.types

interface RuleObserver {
    fun onCreateRule(rule: PlaylistCreationRule)
}