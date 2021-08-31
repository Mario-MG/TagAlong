package com.hfad.tagalong.tools.db.room.utils

import com.hfad.tagalong.tools.db.room.SongEntity

internal object SongTestUtil {
    private const val testSongId1 = "1"
    private const val testSongId2 = "2"
    private const val testSongId3 = "3"
    private const val testSongName1 = "song_one"
    private const val testSongName2 = "song_two"
    private const val testSongName3 = "song_three"
    private const val testAlbum1 = "album_one"
    private const val testAlbum2 = "album_two"
    private const val testAlbum3 = "album_three"
    private const val testArtist1 = "artist_one"
    private const val testArtist2 = "artist_two"
    private const val testArtist3 = "artist_three"
    private const val testImagUrl1 = "image_url_one"
    private const val testImagUrl2 = "image_url_two"
    private const val testImagUrl3 = "image_url_three"

    val testSong1: SongEntity
        get() = SongEntity(testSongId1, testSongName1, testAlbum1, testArtist1, testImagUrl1)

    val testSong2: SongEntity
        get() = SongEntity(testSongId2, testSongName2, testAlbum2, testArtist2, testImagUrl2)

    val testSong3: SongEntity
        get() = SongEntity(testSongId3, testSongName3, testAlbum3, testArtist3, testImagUrl3)

}