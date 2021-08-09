package com.hfad.tagalong.tools.db.room

import androidx.room.*

internal interface BaseDao<T> {
    @Insert
    fun insert(vararg obj: T)

    @Delete
    fun delete(vararg obj: T)
}