package com.hfad.tagalong.types

import com.hfad.tagalong.tools.api.PlaylistManager
import com.hfad.tagalong.tools.api.types.PlaylistGroup

class CustomPlaylist(
    val id: String,
    val name: String,
    val size: Int,
    val imageUrl: String?
) {
    companion object {
        private var total: Int = -1

        fun getAllPlaylistsFromApi(offset: Int = 0, limit: Int = 20): List<CustomPlaylist> {
            val apiResponse = PlaylistManager.getListOfPlaylists(offset, limit)
            if (apiResponse.success) {
                return processApiResponse(apiResponse)
            }
            return emptyList()
        }

        private fun processApiResponse(
            apiResponse: ApiResponse<PlaylistGroup>
        ): List<CustomPlaylist> {
            total = apiResponse.result!!.total
            return createListFromApiResponse(apiResponse)
        }

        private fun createListFromApiResponse(
            apiResponse: ApiResponse<PlaylistGroup>
        ): List<CustomPlaylist> {
            return apiResponse.result!!.items.map { item ->
                CustomPlaylist(
                    item.id,
                    item.name,
                    item.tracks.total,
                    item.images.let { if (it.isNotEmpty()) it[0].url else null }
                )
            }
        }

        fun getTotalPlaylists(): Int {
            return total
        }
    }
}