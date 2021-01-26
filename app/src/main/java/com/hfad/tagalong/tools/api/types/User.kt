package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("display_name") var displayName : String? = null,
    @SerializedName("external_urls") var externalUrls : ExternalUrls,
    @SerializedName("href") var href : String,
    @SerializedName("id") var id : String,
    @SerializedName("type") var type : String,
    @SerializedName("uri") var uri : String
)
