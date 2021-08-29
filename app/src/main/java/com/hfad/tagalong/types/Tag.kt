package com.hfad.tagalong.types

data class Tag (
    val id: Long,
    val name: String
) {
    constructor(name: String) : this(0, name)
}