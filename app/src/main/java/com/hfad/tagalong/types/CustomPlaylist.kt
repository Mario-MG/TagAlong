package com.hfad.tagalong.types

import com.hfad.tagalong.tools.api.PlaylistManager

class CustomPlaylist(
    val id: String,
    val name: String,
    val size: Int,
    val imageUrl: String?
) {
    companion object {
        private var total: Int = -1

        fun getAllPlaylistsFromApi(offset: Int = 0, limit: Int = 20): ArrayList<CustomPlaylist> {
            val playlists = ArrayList<CustomPlaylist>()
            val apiResponse = PlaylistManager.getListOfPlaylists(offset, limit)
            if (apiResponse.success) {
                apiResponse.result!!.items.forEach { item ->
                    playlists.add(CustomPlaylist(
                        item.id,
                        item.name,
                        item.tracks.total,
                        item.images.let { if (it.isNotEmpty()) it[0].url else null }
                    ))
                }
                total = apiResponse.result.total
            }
            return playlists
        }

        fun getTotalPlaylists(): Int {
            return total
        }
    }
}