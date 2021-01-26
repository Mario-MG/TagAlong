package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class PlaylistGroup (
    @SerializedName("href") var href : String,
    @SerializedName("items") var items : List<Playlist>,
    @SerializedName("limit") var limit : Int,
    @SerializedName("next") var next : String,
    @SerializedName("offset") var offset : Int,
    @SerializedName("previous") var previous : String,
    @SerializedName("total") var total : Int
)