package com.hfad.tagalong.tools.api

import com.google.gson.Gson
import com.hfad.tagalong.tools.api.config.Endpoint
import com.hfad.tagalong.tools.api.config.Host
import com.hfad.tagalong.tools.api.types.UserProfile
import okhttp3.OkHttpClient
import okhttp3.Request

object UserManager {
    private val client = OkHttpClient()
    private var USER_PROFILE: UserProfile? = null

    fun getUserID(): String? {
        if (USER_PROFILE == null) {
            getUserProfile()
        }
        return USER_PROFILE?.id
    }

    private fun getUserProfile() {
        val token = TokenManager.getToken()

        val request = Request.Builder()
            .url(URLBuilder()
                .from(Host.API, Endpoint.PROFILE)
                .build())
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response") // TODO: Manejar errores
            USER_PROFILE = Gson().fromJson(response.body?.string(), UserProfile::class.java)
        }
    }
}