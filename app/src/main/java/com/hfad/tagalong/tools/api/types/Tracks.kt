package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class Tracks (
    @SerializedName("href") var href : String,
    @SerializedName("total") var total : Int
)
