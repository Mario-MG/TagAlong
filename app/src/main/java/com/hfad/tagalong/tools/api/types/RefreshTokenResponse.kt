package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse (
    @SerializedName("access_token") var access_token : String,
    @SerializedName("token_type") var token_type : String,
    @SerializedName("expires_in") var expires_in : Int,
    @SerializedName("scope") var scope : String
)