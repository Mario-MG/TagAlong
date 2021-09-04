package com.hfad.tagalong.tools.db.room

import android.content.Context
import androidx.room.*
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory

@Database(entities = [
    TagEntity::class,
    SongEntity::class,
    SongTagCrossRef::class,
    RuleEntity::class,
    RuleTagCrossRef::class
], version = 1)
internal abstract class RoomDb : RoomDatabase() {
    abstract fun tagDao(): TagDao

    abstract fun songDao(): SongDao

    abstract fun songTagCrossRefDao(): SongTagCrossRefDao

    abstract fun ruleDao(): RuleDao

    abstract fun ruleTagCrossRefDao(): RuleTagCrossRefDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDb? = null

        fun getInstance(context: Context): RoomDb =
            INSTANCE ?: synchronized(RoomDb::class.java) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDb::class.java,
                "RoomDatabase.db"
            )
                .openHelperFactory(RequerySQLiteOpenHelperFactory())
                .build()
    }
}