package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class Artist (
    @SerializedName("external_urls") var externalUrls : ExternalUrls,
    @SerializedName("href") var href : String,
    @SerializedName("id") var id : String,
    @SerializedName("name") var name : String,
    @SerializedName("type") var type : String,
    @SerializedName("uri") var uri : String
)