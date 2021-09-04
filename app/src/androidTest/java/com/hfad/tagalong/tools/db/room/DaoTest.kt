package com.hfad.tagalong.tools.db.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.sqlite.db.SimpleSQLiteQuery
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.*
import javax.inject.*

internal abstract class DaoTest<T: DbDao> {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    internal lateinit var db: RoomDb

    internal lateinit var dao: T

    @Before
    open fun setup() {
        hiltRule.inject()
        dao = this.buildDao()
    }

    @After
    open fun clearTables() {
        db.clearAllTables()
        db.openHelper.writableDatabase.execSQL("DELETE FROM Sqlite_Sequence")
    }

    internal abstract fun buildDao(): T
}