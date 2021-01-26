package com.hfad.tagalong.types

import com.hfad.tagalong.tools.api.PlaylistManager

data class CustomTrack(
    val id: String,
    val name: String,
    val album: String,
    val artists: List<String>,
    val imageUrl: String?
) {
    companion object {
        private val totalTracksByPlaylistId = HashMap<String, Int>()

        fun createListOfTracks(playlistId: String, offset: Int = 0, limit: Int = 100): ArrayList<CustomTrack> {
            val tracks = ArrayList<CustomTrack>()
            val apiResponse = PlaylistManager.getPlaylistTracks(playlistId, offset, limit)
            if (apiResponse?.statusCode == 200) {
                apiResponse.result?.items?.forEach { item ->
                    val artists = item.track?.artists?.map { artist -> artist.name }
                    tracks.add(CustomTrack(
                        item.track?.id ?: "null",
                        item.track?.name ?: "null",
                        item.track?.album?.name ?: "null",
                        artists ?: ArrayList(),
                        item.track?.album?.images?.let { if (it.isNotEmpty()) it[0].url else null }
                    ))
                }
                totalTracksByPlaylistId.put(playlistId, apiResponse.result?.total ?: -1)
            }
            return tracks
        }

        fun getTotalTracksForPlaylistId(playlistId: String): Int {
            return totalTracksByPlaylistId[playlistId] ?: -1
        }
    }
}