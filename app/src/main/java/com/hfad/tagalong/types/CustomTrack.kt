package com.hfad.tagalong.types

import com.hfad.tagalong.tools.api.PlaylistManager
import com.hfad.tagalong.tools.api.types.PlaylistItemGroup

data class CustomTrack(
    val id: String,
    val name: String,
    val album: String,
    val artists: List<String>,
    val imageUrl: String?
) {
    companion object {
        private val totalTracksByPlaylistId = HashMap<String, Int>()

        fun getTracksFromApi(playlistId: String, offset: Int = 0, limit: Int = 100): List<CustomTrack> {
            val apiResponse = PlaylistManager.getPlaylistTracks(playlistId, offset, limit)
            if (apiResponse.success) {
                return processApiResponse(apiResponse, playlistId)

            }
            return emptyList()
        }

        private fun processApiResponse(
            apiResponse: ApiResponse<PlaylistItemGroup>,
            playlistId: String
        ): List<CustomTrack> {
            totalTracksByPlaylistId[playlistId] = apiResponse.result!!.total
            return createListFromApiResponse(apiResponse)
        }

        private fun createListFromApiResponse(
            apiResponse: ApiResponse<PlaylistItemGroup>
        ): List<CustomTrack> {
            return apiResponse.result!!.items.map { item ->
                val artists = item.track?.artists?.map { artist -> artist.name }
                CustomTrack(
                    item.track?.id ?: "null",
                    item.track?.name ?: "null",
                    item.track?.album?.name ?: "null",
                    artists ?: ArrayList(),
                    item.track?.album?.images?.let { if (it.isNotEmpty()) it[0].url else null }
                )
            }
        }

        fun getTotalTracksForPlaylistId(playlistId: String): Int {
            return totalTracksByPlaylistId[playlistId] ?: -1
        }
    }
}