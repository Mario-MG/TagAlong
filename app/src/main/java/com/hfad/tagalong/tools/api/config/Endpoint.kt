package com.hfad.tagalong.tools.api.config

object Endpoint {
    const val TOKEN = "/api/token"
    const val GET_PLAYLISTS = "/v1/me/playlists?offset={offset}&limit={limit}"
    const val PLAYLIST_TRACKS = "/v1/playlists/{playlist_id}/tracks"
    const val PROFILE = "/v1/me"
    const val CREATE_PLAYLIST = "/v1/users/{user_id}/playlists"
    const val UNFOLLOW_PLAYLIST = "/v1/playlists/{playlist_id}/followers"
    const val ADD_ITEMS_TO_PLAYLIST = "/v1/playlists/{playlist_id}/tracks"
    const val AUTHORIZE = "/authorize"
}