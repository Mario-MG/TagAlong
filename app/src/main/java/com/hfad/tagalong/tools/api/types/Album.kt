package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class Album (
    @SerializedName("album_type") var albumType : String,
    @SerializedName("artists") var artists : List<Artist>,
    @SerializedName("available_markets") var availableMarkets : List<String>,
    @SerializedName("external_urls") var externalUrls : ExternalUrls,
    @SerializedName("href") var href : String,
    @SerializedName("id") var id : String,
    @SerializedName("images") var images : List<Image>,
    @SerializedName("name") var name : String,
    @SerializedName("type") var type : String,
    @SerializedName("uri") var uri : String
)
