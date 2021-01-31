package com.hfad.tagalong.tools

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hfad.tagalong.types.CustomTrack

class DBHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val readableDB = this.readableDatabase
    private val writableDB = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        createTableSongs(db)
        createTableSongsTags(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun selectAllTags(): ArrayList<String> {
        val tagsList = ArrayList<String>()
        val cursor = readableDB.rawQuery(SQL_SELECT_DISTINCT_TAGS, null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val tagName = cursor.getString(0)
                tagsList.add(tagName)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return tagsList
    }

    fun selectTagNamesBySongId(songId: String, vararg moreSongIds: String): ArrayList<String> {
        val tagsList = ArrayList<String>()
        val selectionArg = moreSongIds.joinToString(", ", songId)
        val cursor = readableDB.rawQuery(SQL_SELECT_TAG_NAMES_BY_SONG_ID, arrayOf(selectionArg))
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val tagName = cursor.getString(0)
                tagsList.add(tagName)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return tagsList
    }

    fun selectSongIdsWithAnyTags(vararg tagNames: String): ArrayList<String>? {
        if (tagNames.isEmpty()) {
            return null
        }
        val songsList = ArrayList<String>()
        val selectionArg = tagNames.joinToString("', '", "'", "'")
        val sql = SQL_SELECT_SONG_IDS_WITH_ANY_TAG_NAMES.format(selectionArg)
        val cursor = readableDB.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val songId = cursor.getString(0)
                songsList.add(songId)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return songsList
    }

    fun selectSongIdsWithAllTags(vararg tagNames: String): ArrayList<String>? {
        if (tagNames.isEmpty()) {
            return null
        } else if (tagNames.size == 1) {
            return selectSongIdsWithAnyTags(tagNames[0])
        }
        val songsList = ArrayList<String>()
        val sql = buildSqlSelectSongIdsWithAllTags(tagNames)
        val cursor = readableDB.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val songId = cursor.getString(0)
                songsList.add(songId)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return songsList
    }

    private fun buildSqlSelectSongIdsWithAllTags(tagNames: Array<out String>): String {
        val sqlSubselects = tagNames.map { tagName -> SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_SUBSELECT.format("'$tagName'") }
        return sqlSubselects.joinToString(
            SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_SEPARATOR,
            SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_MAIN_SELECT,
            ")")
    }

    fun selectSongDataForIds(songId: String, vararg moreSongIds: String): ArrayList<CustomTrack> {
        val songsList = ArrayList<CustomTrack>()
        val selectionArg = moreSongIds.joinToString(", ", songId)
        val cursor = readableDB.rawQuery(SQL_SELECT_SONGS_BY_ID, arrayOf(selectionArg))
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val songName = cursor.getString(0)
                val songAlbum = cursor.getString(1)
                val songArtists = cursor.getString(2).split(", ")
                val songImageUrl = cursor.getString(3)
                songsList.add(
                    CustomTrack(
                        songId,
                        songName,
                        songAlbum,
                        songArtists,
                        songImageUrl
                    )
                )
                cursor.moveToNext()
            }
        }
        cursor.close()
        return songsList
    }

    fun selectSongDataByTagNames(tagName: String, vararg moreTagNames: String): ArrayList<CustomTrack> {
        val songsList = ArrayList<CustomTrack>()
        val selectionArg = moreTagNames.joinToString(", ", tagName)
        val cursor = readableDB.rawQuery(SQL_SELECT_SONGS_BY_TAG_NAME, arrayOf(selectionArg))
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val songId = cursor.getString(0)
                val songName = cursor.getString(1)
                val songAlbum = cursor.getString(2)
                val songArtists = cursor.getString(3).split(", ")
                val songImageUrl = cursor.getString(4)
                songsList.add(
                    CustomTrack(
                        songId,
                        songName,
                        songAlbum,
                        songArtists,
                        songImageUrl
                    )
                )
                cursor.moveToNext()
            }
        }
        cursor.close()
        return songsList
    }

    fun insertSongWithTag(track: CustomTrack, tagName: String) {
        insertIntoSongsTags(track.id, tagName)
        insertSongData(track)
    }

    private fun insertIntoSongsTags(songId: String, tagName: String) {
        writableDB.insertOrThrow(TABLE_SONGS_TAGS, null, ContentValues().apply {
            put(SONG_ID, songId)
            put(TAG_NAME, tagName)
        })
    }

    private fun insertSongData(track: CustomTrack) {
        try {
            writableDB.insertOrThrow(TABLE_SONGS, null, ContentValues().apply {
                put(SONG_ID, track.id)
                put(SONG_NAME, track.name)
                put(SONG_ALBUM, track.album)
                put(SONG_ARTISTS, track.artists.joinToString(", "))
                put(SONG_IMAGE_URL, track.imageUrl)
            })
        } catch (ex: SQLiteConstraintException) {}
    }

    fun deleteSongWithTag(songId: String, tagName: String) {
        writableDB.delete(TABLE_SONGS_TAGS, "$SONG_ID=? AND $TAG_NAME=?", arrayOf(songId, tagName))
        // TODO: Borrar datos de Songs si ya no tiene más tags?
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "tagalong.db"
        const val TABLE_SONGS = "Songs"
        const val SONG_ID = "song_id"
        const val SONG_NAME = "name"
        const val SONG_ALBUM = "album"
        const val SONG_ARTISTS = "artists"
        const val SONG_IMAGE_URL = "image_url"
        const val TABLE_SONGS_TAGS = "Songs_Tags"
        const val TAG_NAME = "tag_name"

        private const val SQL_CREATE_SONGS =
            """
                CREATE TABLE IF NOT EXISTS $TABLE_SONGS (
                    $SONG_ID VARCHAR PRIMARY KEY,
                    $SONG_NAME VARCHAR NOT NULL,
                    $SONG_ALBUM VARCHAR,
                    $SONG_ARTISTS VARCHAR,
                    $SONG_IMAGE_URL VARCHAR
                )
            """

        private const val SQL_SELECT_SONGS_BY_ID =
            """
                SELECT $SONG_ID, $SONG_NAME, $SONG_ALBUM, $SONG_ARTISTS, $SONG_IMAGE_URL
                    FROM $TABLE_SONGS WHERE $SONG_ID IN (?)
            """

        private const val SQL_SELECT_SONGS_BY_TAG_NAME =
            """
                SELECT songs.$SONG_ID, $SONG_NAME, $SONG_ALBUM, $SONG_ARTISTS, $SONG_IMAGE_URL
                    FROM $TABLE_SONGS songs
                    JOIN $TABLE_SONGS_TAGS songs_tags ON songs.$SONG_ID = songs_tags.$SONG_ID
                    WHERE songs_tags.$TAG_NAME IN (?)
            """

        fun createTableSongs(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_SONGS)
        }

        private const val SQL_CREATE_SONGS_TAGS =
            """
                CREATE TABLE IF NOT EXISTS $TABLE_SONGS_TAGS (
                    $SONG_ID VARCHAR CONSTRAINT fk_songs_tags_song_id REFERENCES $TABLE_SONGS($SONG_ID),
                    $TAG_NAME VARCHAR,
                    CONSTRAINT pk_songs_tags PRIMARY KEY ($SONG_ID, $TAG_NAME)
                )
            """

        private const val SQL_SELECT_DISTINCT_TAGS =
            """
                SELECT DISTINCT $TAG_NAME FROM $TABLE_SONGS_TAGS
            """

        private const val SQL_SELECT_TAG_NAMES_BY_SONG_ID =
            """
                SELECT $TAG_NAME FROM $TABLE_SONGS_TAGS
                    WHERE $SONG_ID IN (?)
            """

        private const val SQL_SELECT_SONG_IDS_WITH_ANY_TAG_NAMES =
            """
                SELECT DISTINCT $SONG_ID FROM $TABLE_SONGS_TAGS
                    WHERE $TAG_NAME IN (%s)
            """

        private const val SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_MAIN_SELECT =
            """
                SELECT DISTINCT $SONG_ID FROM $TABLE_SONGS_TAGS
                    WHERE $SONG_ID IN (
            """

        private const val SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_SEPARATOR =
            """
                ) AND $SONG_ID IN (
            """

        private const val SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_SUBSELECT =
            """
                SELECT $SONG_ID FROM $TABLE_SONGS_TAGS
                    WHERE $TAG_NAME = %s
            """

        fun createTableSongsTags(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_SONGS_TAGS)
        }
    }
}