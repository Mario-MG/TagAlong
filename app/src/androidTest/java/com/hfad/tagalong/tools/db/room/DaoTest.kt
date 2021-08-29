package com.hfad.tagalong.tools.db.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
    fun setup() {
        hiltRule.inject()
        dao = this.buildDao()
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    internal abstract fun buildDao(): T
}