package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class Track (
    @SerializedName("album") var album : Album?,
    @SerializedName("artists") var artists : List<Artist>?,
    @SerializedName("available_markets") var availableMarkets : List<String>,
    @SerializedName("disc_number") var discNumber : Int,
    @SerializedName("duration_ms") var durationMs : Int,
    @SerializedName("explicit") var explicit : Boolean,
    @SerializedName("external_ids") var externalIds : ExternalIds,
    @SerializedName("external_urls") var externalUrls : ExternalUrls,
    @SerializedName("href") var href : String,
    @SerializedName("id") var id : String?,
    @SerializedName("name") var name : String?,
    @SerializedName("popularity") var popularity : Int,
    @SerializedName("preview_url") var previewUrl : String?,
    @SerializedName("track_number") var trackNumber : Int,
    @SerializedName("type") var type : String,
    @SerializedName("uri") var uri : String
)
