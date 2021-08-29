package com.hfad.tagalong.tools.db.room

import androidx.room.*

internal interface BaseDao<T: DbEntity>: DbDao {
    @Insert
    fun insert(obj: T): Long

    @Insert
    fun insertAll(vararg obj: T): List<Long>
}