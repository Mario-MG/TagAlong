package com.hfad.tagalong.tools.db.room

internal abstract class SongDaoTest : DaoTest<SongDao>() {
    companion object {
        const val testSongId1 = "1"
        const val testSongId2 = "2"
        const val testSongId3 = "3"
        const val testSongName1 = "one"
        const val testSongName2 = "two"
        const val testSongName3 = "three"
        const val testAlbum1 = "album_one"
        const val testAlbum2 = "album_two"
        const val testAlbum3 = "album_three"
        const val testArtist1 = "artist_one"
        const val testArtist2 = "artist_two"
        const val testArtist3 = "artist_three"
        const val testImagUrl1 = "image_url_one"
        const val testImagUrl2 = "image_url_two"
        const val testImagUrl3 = "image_url_three"
    }

    val testSong1: SongEntity
        get() = SongEntity(testSongId1, testSongName1, testAlbum1, testArtist1, testImagUrl1)

    val testSong2: SongEntity
        get() = SongEntity(testSongId2, testSongName2, testAlbum2, testArtist2, testImagUrl2)

    val testSong3: SongEntity
        get() = SongEntity(testSongId3, testSongName3, testAlbum3, testArtist3, testImagUrl3)

    final override fun buildDao(): SongDao = db.songDao()
}