package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class UserProfile (
    @SerializedName("country") var country : String,
    @SerializedName("display_name") var displayName : String?,
    @SerializedName("email") var email : String,
    @SerializedName("external_urls") var externalUrls : ExternalUrls,
    @SerializedName("followers") var followers : Followers,
    @SerializedName("href") var href : String,
    @SerializedName("id") var id : String,
    @SerializedName("images") var images : List<Image>,
    @SerializedName("product") var product : String,
    @SerializedName("type") var type : String,
    @SerializedName("uri") var uri : String
)