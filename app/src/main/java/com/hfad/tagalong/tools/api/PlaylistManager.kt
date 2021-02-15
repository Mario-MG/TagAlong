package com.hfad.tagalong.tools.api

import com.hfad.tagalong.tools.api.config.*
import com.hfad.tagalong.tools.api.types.*
import com.hfad.tagalong.types.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

object PlaylistManager {
    fun getListOfPlaylists(offset: Int = 0, limit: Int = 20): ApiResponse<PlaylistGroup> {
        val token = TokenManager.getToken()

        val request = Request.Builder()
            .url(URLBuilder()
                .from(Host.API, Endpoint.GET_PLAYLISTS)
                .replace("offset", offset.coerceAtMost(100000))
                .replace("limit", limit.coerceAtMost(50))
                .build())
            .header("Authorization", "Bearer $token")
            .build()

        return RequestManager.sendRequest(request)
    }

    fun getPlaylistTracks(playlistId: String, offset: Int = 0, limit: Int = 100): ApiResponse<PlaylistItemGroup> {
        val token = TokenManager.getToken()

        val request = Request.Builder()
            .url(URLBuilder()
                .from(Host.API, Endpoint.PLAYLIST_TRACKS)
                .replace("playlist_id", playlistId)
                .param("offset", offset)
                .param("limit", limit.coerceAtMost(100))
                .build())
            .header("Authorization", "Bearer $token")
            .build()

        return RequestManager.sendRequest(request)
    }

    fun createPlaylist(
        name: String,
        description: String = "",
        public: Boolean = true,
        collaborative: Boolean = false
    ): ApiResponse<Playlist>? {
        val token = TokenManager.getToken()

        val body = """
            {
                "name": "$name",
                "description": "$description",
                "public": $public,
                "collaborative": $collaborative
            }
        """.trimIndent().toRequestBody(ContentType.CONTENT_TYPE_JSON.toMediaType())

        val userID = UserManager.getUserID()
        if (userID !== null) {
            val request = Request.Builder()
                .url(URLBuilder()
                    .from(Host.API, Endpoint.CREATE_PLAYLIST)
                    .replace("user_id", userID)
                    .build())
                .header("Authorization", "Bearer $token")
                .post(body)
                .build()

            return RequestManager.sendRequest(request)
        }
        return null // TODO: Lanzar excepción en caso de userID nulo
    }

    fun unfollowPlaylist(playlistId: String): ApiResponse<Any?> { // TODO: Revisar uso de Any
        val token = TokenManager.getToken()

        val request = Request.Builder()
            .url(URLBuilder()
                .from(Host.API, Endpoint.UNFOLLOW_PLAYLIST)
                .replace("playlist_id", playlistId)
                .build())
            .header("Authorization", "Bearer $token")
            .delete()
            .build()

        return RequestManager.sendRequest(request)
    }

    fun addTracksToPlaylist(playlistId: String, vararg songIds: String): ApiResponse<Snapshot>? {
        if (songIds.isEmpty() || songIds.size > 100) { // TODO: Manejar caso > 100
            return null // TODO: Lanzar excepción
        }

        val token = TokenManager.getToken()

        val songUris = songIds.map { songId -> "\"spotify:track:$songId\""}

        val body = """
            {
                "uris": [${songUris.joinToString(",")}]
            }
        """.trimIndent().toRequestBody(ContentType.CONTENT_TYPE_JSON.toMediaType())

        val request = Request.Builder()
            .url(URLBuilder()
                .from(Host.API, Endpoint.ADD_ITEMS_TO_PLAYLIST)
                .replace("playlist_id", playlistId)
                .build())
            .header("Authorization", "Bearer $token")
            .post(body)
            .build()

        return RequestManager.sendRequest(request)
    }
}