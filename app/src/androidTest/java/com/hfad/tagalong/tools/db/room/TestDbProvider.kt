package com.hfad.tagalong.tools.db.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestDbProvider {
    @Volatile
    private var INSTANCE: RoomDb? = null

    @Provides
    @Named("test_db")
    internal fun provideInMemoryDb(@ApplicationContext context: Context) =
        TestDbProvider.INSTANCE ?: synchronized(TestDbProvider::class.java) {
            TestDbProvider.INSTANCE ?: TestDbProvider.buildDatabase(context).also { TestDbProvider.INSTANCE = it }
        }

    private fun buildDatabase(context: Context) =
        Room.inMemoryDatabaseBuilder(context, RoomDb::class.java)
            .openHelperFactory(RequerySQLiteOpenHelperFactory())
            .allowMainThreadQueries()
            .build()
}