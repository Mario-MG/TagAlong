package com.hfad.tagalong.types

interface RuleSubject {
    fun subscribe(observer: RuleObserver)
    fun unsubscribe(observer: RuleObserver)
    fun notifyObservers(rule: PlaylistCreationRule)
}