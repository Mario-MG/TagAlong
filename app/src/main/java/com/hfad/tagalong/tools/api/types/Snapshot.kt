package com.hfad.tagalong.tools.api.types

import com.google.gson.annotations.SerializedName

data class Snapshot (
    @SerializedName("snapshot_id") var snapshot_id : String
)