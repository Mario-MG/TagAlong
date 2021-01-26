package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class PlaylistItem (
    @SerializedName("added_at") var added_at : String?,
    @SerializedName("added_by") var added_by : User?,
    @SerializedName("is_local") var is_local : Boolean,
    @SerializedName("track") var track : Track?
)
