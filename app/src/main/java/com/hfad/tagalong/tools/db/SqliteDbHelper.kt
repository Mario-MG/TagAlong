package com.hfad.tagalong.tools.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hfad.tagalong.config.Optionality
import com.hfad.tagalong.types.Track
import com.hfad.tagalong.types.PlaylistCreationRule

class SqliteDbHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val readableDB = this.readableDatabase
    private val writableDB = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        createTableSongs(db)
        createTableSongsTags(db)
        createTableRules(db)
        createTableRulesTags(db)
        // TODO: Crear tabla para tags
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        createTableRules(db)
        createTableRulesTags(db)
    }

    fun selectAllTags(): ArrayList<String> {
        return runSimpleSelect(SQL_SELECT_DISTINCT_TAGS)
    }

    fun selectTagNamesBySongId(vararg songIds: String): ArrayList<String> {
        return runSimpleSelect(SQL_SELECT_TAG_NAMES_BY_SONG_ID, songIds)
    }

    fun selectSongIdsWithAnyTags(vararg tagNames: String): ArrayList<String> {
        return runSimpleSelect(SQL_SELECT_SONG_IDS_WITH_ANY_TAG_NAMES, tagNames)
    }

    fun selectSongIdsWithAllTags(vararg tagNames: String): ArrayList<String> {
        if (tagNames.size == 1) {
            return selectSongIdsWithAnyTags(tagNames[0])
        }
        val sql = buildSqlSelectSongIdsWithAllTags(tagNames)
        return runSimpleSelect(sql)
    }

    private fun buildSqlSelectSongIdsWithAllTags(tagNames: Array<out String>): String {
        val sqlSubselects = tagNames.map { tagName ->
            SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_SUBSELECT.format("'$tagName'")
        }
        return sqlSubselects.joinToString(
            SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_SEPARATOR,
            SQL_SELECT_SONG_IDS_WITH_ALL_TAG_NAMES_MAIN_SELECT,
            ")")
    }

    private fun runSimpleSelect(
        baseSql: String,
        selectionArgs: Array<out String>? = null
    ): ArrayList<String> {
        return when {
            selectionArgs == null -> getStringArrayListFromSql(baseSql)
            selectionArgs.isEmpty() -> emptyList<String>() as ArrayList<String>
            else -> {
                val selectionArgsString = selectionArgs.joinToString("', '", "'", "'")
                val sql = baseSql.format(selectionArgsString)
                getStringArrayListFromSql(sql)
            }
        }
    }

    private fun runSimpleSelect(
        baseSql: String,
        selectionArg: String
    ): ArrayList<String> {
        return runSimpleSelect(baseSql, arrayOf(selectionArg))
    }

    private fun getStringArrayListFromSql(sql: String): ArrayList<String> {
        val stringArrayList = ArrayList<String>()
        val cursor = readableDB.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val stringItem = cursor.getString(0)
                stringArrayList.add(stringItem)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return stringArrayList
    }

    fun selectSongDataForIds(songId: String, vararg moreSongIds: String): ArrayList<Track> {
        val songsList = ArrayList<Track>()
        val selectionArg = moreSongIds.joinToString(", ", songId)
        val cursor = readableDB.rawQuery(SQL_SELECT_SONGS_BY_ID, arrayOf(selectionArg))
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val songName = cursor.getString(0)
                val songAlbum = cursor.getString(1)
                val songArtists = cursor.getString(2).split(", ")
                val songImageUrl = cursor.getString(3)
                songsList.add(
                    Track(
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

    fun selectSongDataByTagNames(tagName: String, vararg moreTagNames: String): ArrayList<Track> {
        val songsList = ArrayList<Track>()
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
                    Track(
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

    fun insertSongWithTag(track: Track, tagName: String) {
        insertIntoSongsTags(track.id, tagName)
        insertSongData(track)
    }

    private fun insertIntoSongsTags(songId: String, tagName: String) {
        val insertValues = ContentValues().apply {
            put(SONG_ID, songId)
            put(TAG_NAME, tagName)
        }
        writableDB.insertOrThrow(TABLE_SONGS_TAGS, null, insertValues)
    }

    private fun insertSongData(track: Track) {
        val insertValues = ContentValues().apply {
            put(SONG_ID, track.id)
            put(SONG_NAME, track.name)
            put(SONG_ALBUM, track.album)
            put(SONG_ARTISTS, track.artists.joinToString(", "))
            put(SONG_IMAGE_URL, track.imageUrl)
        }
        try {
            writableDB.insertOrThrow(TABLE_SONGS, null, insertValues)
        } catch (ex: SQLiteConstraintException) {}
    }

    fun deleteSongWithTag(songId: String, tagName: String) {
        writableDB.delete(TABLE_SONGS_TAGS, "$SONG_ID=? AND $TAG_NAME=?", arrayOf(songId, tagName))
        // TODO: Borrar datos de Songs si ya no tiene más tags?
    }

    fun selectAllRulesWithTags(): ArrayList<PlaylistCreationRule> {
        val rulesTagsMap = selectAllRulesTags()
        val rules = selectAllRulesAndAddTags(rulesTagsMap)
        return rules
    }

    private fun selectAllRulesTags(): HashMap<Long, ArrayList<String>> {
        val rulesTagsMap = HashMap<Long, ArrayList<String>>()
        val cursor = readableDB.rawQuery(SQL_SELECT_ALL_RULES_TAGS, null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val ruleId = cursor.getLong(0)
                if (!rulesTagsMap.containsKey(ruleId)) {
                    rulesTagsMap[ruleId] = ArrayList()
                }
                val tag = cursor.getString(1)
                rulesTagsMap[ruleId]!!.add(tag)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return rulesTagsMap
    }

    private fun selectAllRulesAndAddTags(rulesTagsMap: HashMap<Long, ArrayList<String>>): ArrayList<PlaylistCreationRule> {
        val rules = ArrayList<PlaylistCreationRule>()
        val cursor = readableDB.rawQuery(SQL_SELECT_ALL_RULES, null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val ruleId = cursor.getLong(0)
                val playlistId = cursor.getString(1)
                val optionalityInt = cursor.getInt(2)
                val optionality = Optionality.values().find { op -> op.ordinal == optionalityInt }
                val autoUpdateInt = cursor.getInt(3)
                val autoUpdate = autoUpdateInt != 0
                val rule = PlaylistCreationRule(
                    ruleId,
                    rulesTagsMap[ruleId] ?: emptyList<String>() as ArrayList<String>,
                    playlistId,
                    optionality ?: Optionality.ANY,
                    autoUpdate
                )
                rules.add(rule)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return rules
    }

    fun insertRule(rule: PlaylistCreationRule): Long {
        val ruleId = insertIntoRules(rule)
        rule.ruleId = ruleId
        insertIntoRulesTags(rule)
        return ruleId
    }

    private fun insertIntoRules(rule: PlaylistCreationRule): Long {
        val insertValues = ContentValues().apply {
            put(PLAYLIST_ID, rule.playlistId)
            put(OPTIONALITY, rule.optionality.value)
            put(AUTO_UPDATE, rule.autoUpdate)
        }
        return writableDB.insert(TABLE_RULES, null, insertValues)
    }

    private fun insertIntoRulesTags(rule: PlaylistCreationRule) {
        rule.tags.forEach { tag ->
            val insertValues = ContentValues().apply {
                put(RULE_ID, rule.ruleId)
                put(TAG_NAME, tag)
            }
            writableDB.insert(TABLE_RULES_TAGS, null, insertValues)
        }
    }

    private fun deleteRule(ruleId: Int) {
        // TODO
    }

    fun selectPlaylistsToAddSongWithTag(songId: String, tagName: String): HashSet<String> {
        val playlistsFulfillingAnyRule = selectPlaylistsFulfillingAnyRule(tagName)
        val playlistsFulfillingAllRule = selectPlaylistsFulfillingAllRule(songId, tagName)
        return (playlistsFulfillingAnyRule + playlistsFulfillingAllRule) as HashSet
    }

    private fun selectPlaylistsFulfillingAnyRule(tagName: String): Set<String> {
        return runSimpleSelect(SQL_SELECT_PLAYLISTS_FOR_TAG_ANY, tagName).toSet()
    }

    private fun selectPlaylistsFulfillingAllRule(songId: String, tagName: String): Set<String> {
        val tagNamesForSong = selectTagNamesBySongId(songId) + tagName
        return runSimpleSelect(SQL_SELECT_PLAYLISTS_FOR_TAG_ALL, tagNamesForSong.toTypedArray()).toSet()
    }

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "tagalong.db"
        private const val TABLE_SONGS = "Songs"
        private const val TABLE_SONGS_TAGS = "Songs_Tags"
        private const val TABLE_RULES = "Rules"
        private const val TABLE_RULES_TAGS = "Rules_Tags"
        private const val SONG_ID = "song_id"
        private const val SONG_NAME = "name"
        private const val SONG_ALBUM = "album"
        private const val SONG_ARTISTS = "artists"
        private const val SONG_IMAGE_URL = "image_url"
        private const val TAG_NAME = "tag_name"
        private const val RULE_ID = "rule_id"
        private const val PLAYLIST_ID = "playlist_id"
        private const val OPTIONALITY = "optionality"
        private const val AUTO_UPDATE = "auto_update"

        // TODO: Crear tabla para tags

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
                    FROM $TABLE_SONGS
                    WHERE $SONG_ID IN (?)
            """

        private const val SQL_SELECT_SONGS_BY_TAG_NAME =
            """
                SELECT songs.$SONG_ID, $SONG_NAME, $SONG_ALBUM, $SONG_ARTISTS, $SONG_IMAGE_URL
                    FROM $TABLE_SONGS songs
                    JOIN $TABLE_SONGS_TAGS songs_tags ON songs.$SONG_ID = songs_tags.$SONG_ID
                    WHERE songs_tags.$TAG_NAME IN (?)
            """

        private const val SQL_CREATE_SONGS_TAGS =
            """
                CREATE TABLE IF NOT EXISTS $TABLE_SONGS_TAGS (
                    $SONG_ID VARCHAR CONSTRAINT fk_songs_tags_song_id REFERENCES $TABLE_SONGS($SONG_ID),
                    $TAG_NAME VARCHAR,
                    CONSTRAINT pk_songs_tags PRIMARY KEY ($SONG_ID, $TAG_NAME)
                )
            """

        private const val SQL_CREATE_RULES =
            """
                CREATE TABLE IF NOT EXISTS $TABLE_RULES (
                    $RULE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $PLAYLIST_ID VARCHAR NOT NULL,
                    $OPTIONALITY BOOLEAN,
                    $AUTO_UPDATE BOOLEAN
                )
            """

        private const val SQL_CREATE_RULES_TAGS =
            """
                CREATE TABLE IF NOT EXISTS $TABLE_RULES_TAGS (
                    $RULE_ID VARCHAR
                        CONSTRAINT fk_rules_tags_rule_id
                            REFERENCES $TABLE_RULES($RULE_ID)
                            ON DELETE CASCADE,
                    $TAG_NAME VARCHAR,
                    CONSTRAINT pk_rules_tags PRIMARY KEY ($RULE_ID, $TAG_NAME)
                )
            """

        private const val SQL_SELECT_DISTINCT_TAGS =
            """
                SELECT DISTINCT $TAG_NAME FROM $TABLE_SONGS_TAGS
            """

        private const val SQL_SELECT_TAG_NAMES_BY_SONG_ID =
            """
                SELECT $TAG_NAME FROM $TABLE_SONGS_TAGS
                    WHERE $SONG_ID IN (%s)
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

        private const val SQL_SELECT_ALL_RULES =
            """
                SELECT * FROM $TABLE_RULES
            """

        private const val SQL_SELECT_ALL_RULES_TAGS =
            """
                SELECT * FROM $TABLE_RULES_TAGS
            """

        private const val SQL_SELECT_PLAYLISTS_FOR_TAG_ANY =
            """
                SELECT r.$PLAYLIST_ID FROM $TABLE_RULES r
                    JOIN $TABLE_RULES_TAGS rt
                        ON r.$RULE_ID = rt.$RULE_ID
                    WHERE r.$AUTO_UPDATE = 1
                        AND r.$OPTIONALITY = 1
                        AND rt.$TAG_NAME = %s
            """

        private const val SQL_SELECT_PLAYLISTS_FOR_TAG_ALL =
            """
                SELECT $PLAYLIST_ID FROM $TABLE_RULES
                    WHERE $AUTO_UPDATE = 1
                        AND $OPTIONALITY = 0
                        AND $RULE_ID NOT IN (
                            SELECT DISTINCT $RULE_ID FROM $TABLE_RULES_TAGS
                                WHERE $TAG_NAME NOT IN (%s)
                            )
            """ // TODO: Evitar duplicados cada vez que se añade una nueva etiqueta (comprobar si la nueva etiqueta forma parte de la regla)

        private fun createTableSongs(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_SONGS)
        }

        private fun createTableSongsTags(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_SONGS_TAGS)
        }

        private fun createTableRules(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_RULES)
        }

        private fun createTableRulesTags(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_RULES_TAGS)
        }
    }
}