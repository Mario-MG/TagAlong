package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class ExternalIds (
    @SerializedName("isrc") var isrc : String
)